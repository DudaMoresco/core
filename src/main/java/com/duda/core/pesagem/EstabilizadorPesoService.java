package com.duda.core.pesagem;

import com.duda.core.pesagem.domain.criterioestabilizacao.CriterioEstabilizacao;
import com.duda.core.pesagem.domain.EstadoBalanca;
import com.duda.core.pesagem.domain.LeituraDto;
import com.duda.core.pesagem.domain.LeituraPeso;
import com.duda.core.pesagem.domain.PesoEstabilizadoDTO;
import com.duda.core.pesagem.repository.EstadoBalancaRepository;
import com.duda.core.shared.utils.CalculadoraEstatistica;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EstabilizadorPesoService {

    private final EstadoBalancaRepository repository;
    private final List<CriterioEstabilizacao> criterios;

    public Optional<PesoEstabilizadoDTO> processarLeitura(LeituraDto leitura) {
        EstadoBalanca estado = obterOuCriarEstado(leitura);
        adicionarLeitura(estado, leitura);
        removerLeiturasAntigas(estado);

        if (estaEstabilizado(estado)) {
            return gerarResultado(estado);
        }

        return Optional.empty();
    }

    private EstadoBalanca obterOuCriarEstado(LeituraDto leitura) {
        return repository.buscar(leitura.id())
                .orElseGet(() -> {
                    EstadoBalanca novo = new EstadoBalanca();
                    novo.setId(leitura.id());
                    novo.setPlaca(leitura.plate());
                    repository.salvar(leitura.id(), novo);
                    return novo;
                });
    }

    private void adicionarLeitura(EstadoBalanca estado, LeituraDto leitura) {
        LeituraPeso leituraPeso = new LeituraPeso(
                leitura.weight(),
                leitura.timestamp()
        );
        estado.getLeituras().add(leituraPeso);
    }

    private void removerLeiturasAntigas(EstadoBalanca estado) {
        double janelaSegundos = 2.0;
        LocalDateTime limiteTempo = LocalDateTime.now()
                .minusNanos((long) (janelaSegundos * 1_000_000_000));
        estado.getLeituras().removeIf(r -> r.getTimestamp().isBefore(limiteTempo));
    }

    private boolean estaEstabilizado(EstadoBalanca estado) {
        return criterios.stream()
                .allMatch(criterio -> criterio.avaliar(estado));
    }

    private Optional<PesoEstabilizadoDTO> gerarResultado(EstadoBalanca estado) {
        List<BigDecimal> pesos = estado.getLeituras().stream()
                .map(LeituraPeso::getPeso)
                .toList();

        BigDecimal pesoEstabilizado = CalculadoraEstatistica.calcularMediana(pesos);

        if (estado.getUltimoPesoEstabilizado() != null &&
                estado.getUltimoPesoEstabilizado().equals(pesoEstabilizado)) {
            return Optional.empty();
        }

        estado.setUltimoPesoEstabilizado(pesoEstabilizado);
        estado.setUltimaEstabilizacao(LocalDateTime.now());

        BigDecimal desvioPadrao = CalculadoraEstatistica.calcularDesvioPadrao(pesos);

        return Optional.of(new PesoEstabilizadoDTO(
                estado.getId(),
                estado.getPlaca(),
                pesoEstabilizado,
                estado.getUltimaEstabilizacao(),
                estado.getLeituras().size(),
                desvioPadrao
        ));
    }
}