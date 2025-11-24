package com.serasa.core.pesagem;

import com.serasa.core.balanca.MedicaoBalancaEntity;
import com.serasa.core.balanca.MedicaoBalancaRepository;
import com.serasa.core.caminhao.CaminhaoResponseDto;
import com.serasa.core.caminhao.CaminhaoService;
import com.serasa.core.demandatransporte.DemandaTransporteEntity;
import com.serasa.core.demandatransporte.DemandaTransporteService;
import com.serasa.core.demandatransporte.StatusDemandaTransporte;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class EstabilizacaoWorker {

    private final MedicaoBalancaRepository medicaoBalancaRepository;
    private final DemandaTransporteService demandaTransporteService;
    private final CaminhaoService caminhaoService;
    private final PesagemRepository pesagemRepository;

    @Autowired
    public EstabilizacaoWorker(MedicaoBalancaRepository medicaoBalancaRepository, DemandaTransporteService demandaTransporteService,
                               CaminhaoService caminhaoService, PesagemRepository pesagemRepository) {
        this.medicaoBalancaRepository = medicaoBalancaRepository;
        this.demandaTransporteService = demandaTransporteService;
        this.caminhaoService = caminhaoService;
        this.pesagemRepository = pesagemRepository;
    }

    private static final double THRESHOLD_DESVIO = 5.0; // kg
    private static final int JANELA_SEGUNDOS = 10;

    /* deveriamos executar a cada 10 segundos ? como identificamos quais medições vamos processar ?
        acredito que um evento de start da medição seja necessário ? 
    */

    private void processarBalanca(Long idBalanca) {

        //como funcionaria com timezones ?
        LocalDateTime inicio = LocalDateTime.now().minusSeconds(JANELA_SEGUNDOS);

        List<MedicaoBalancaEntity> medicoes = medicaoBalancaRepository.findByIdBalancaAndCreatedAtAfter(idBalanca, inicio);

        // Precisa de pelo menos 100 medições (10s * 100 medições/s)
        if (medicoes.size() < 100) {
            return;
        }

        double desvio = calcularDesvioPadrao(medicoes);

        // Verifica estabilização
        if (desvio < THRESHOLD_DESVIO) {
            double media = calcularMedia(medicoes);
            finalizarPesagem(idBalanca, medicoes, media, desvio);
        }
    }

    private double calcularDesvioPadrao(List<MedicaoBalancaEntity> medicoes) {
        double media = calcularMedia(medicoes);

        double somaQuadrados = medicoes.stream()
                .mapToDouble(p -> Math.pow(p.getPeso().doubleValue() - media, 2))
                .sum();

        return Math.sqrt(somaQuadrados / medicoes.size());
    }

    private double calcularMedia(List<MedicaoBalancaEntity> medicoes) {
        return medicoes.stream()
                .mapToDouble(p -> p.getPeso().doubleValue())
                .average()
                .orElse(0.0);
    }

    @Transactional
    private void finalizarPesagem(Long idBalanca, List<MedicaoBalancaEntity> medicoes,
                                  double pesoFinal, double desvio) {
        MedicaoBalancaEntity ultimaMedicao = medicoes.get(0);

        DemandaTransporteEntity demanda = demandaTransporteService
                .getByCaminhaoPlacaAndStatus(
                        ultimaMedicao.getPlaca(),
                        StatusDemandaTransporte.EM_ANDAMENTO
                );
//                .orElseThrow(() -> new RuntimeException("Demanda não encontrada"));

        CaminhaoResponseDto caminhao = caminhaoService.getCaminhaoByPlaca(ultimaMedicao.getPlaca())
                .orElseThrow(() -> new RuntimeException("Demanda não encontrada"));


        PesagemEntity consolidada = new PesagemEntity();
        consolidada.setIdBalanca(idBalanca);
        consolidada.setIdDemanda(demanda.getId());
        consolidada.setIdCaminhao(caminhao.getId());
        consolidada.setIdGrao(demanda.getIdGrao());
        consolidada.setIdFilial(demanda.getIdFilial());

        BigDecimal tara = caminhao.getTara();
        consolidada.setPesoBruto(BigDecimal.valueOf(pesoFinal));
        consolidada.setTara(tara);

        consolidada.setCreatedBy(String.valueOf(idBalanca));

        pesagemRepository.save(consolidada);

        demandaTransporteService.finalizarPesagem(demanda.getId());
    }
}
