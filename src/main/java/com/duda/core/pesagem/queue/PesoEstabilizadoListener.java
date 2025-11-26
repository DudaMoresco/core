package com.duda.core.pesagem.queue;

import com.duda.core.balanca.BalancaService;
import com.duda.core.balanca.dto.BalancaResponseDto;
import com.duda.core.caminhao.CaminhaoService;
import com.duda.core.caminhao.dto.CaminhaoResponseDto;
import com.duda.core.config.RabbitMQConfig;
import com.duda.core.demandatransporte.DemandaTransporteService;
import com.duda.core.demandatransporte.entity.DemandaTransporteEntity;
import com.duda.core.demandatransporte.enumeration.StatusDemandaTransporte;
import com.duda.core.grao.GraoService;
import com.duda.core.grao.dto.GraoResponseDto;
import com.duda.core.pesagem.domain.PesoEstabilizadoDTO;
import com.duda.core.pesagem.entity.PesagemEntity;
import com.duda.core.pesagem.repository.PesagemRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
public class PesoEstabilizadoListener {

    private final Logger logger = LoggerFactory.getLogger(PesoEstabilizadoListener.class);

    private final DemandaTransporteService demandaTransporteService;
    private final CaminhaoService caminhaoService;
    private final GraoService graoService;
    private final BalancaService balancaService;
    private final PesagemRepository pesagemRepository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public PesoEstabilizadoListener(DemandaTransporteService demandaTransporteService, CaminhaoService caminhaoService,
                                    GraoService graoService, BalancaService balancaService,
                                    PesagemRepository pesagemRepository, RabbitTemplate rabbitTemplate) {
        this.demandaTransporteService = demandaTransporteService;
        this.caminhaoService = caminhaoService;
        this.graoService = graoService;
        this.balancaService = balancaService;
        this.pesagemRepository = pesagemRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.FILA_BALANCA_ESTABILIZADA)
    @Transactional
    public void processarPesoEstabilizado(PesoEstabilizadoDTO pesoEstabilizado) {
        logger.info("PESO ESTABILIZADO ");

        DemandaTransporteEntity demanda = demandaTransporteService
                .getByCaminhaoPlacaAndStatus(
                        pesoEstabilizado.placa(),
                        StatusDemandaTransporte.EM_ANDAMENTO
                );

        CaminhaoResponseDto caminhao = caminhaoService.getCaminhaoByPlaca(pesoEstabilizado.placa())
                .orElseThrow(() -> new RuntimeException("Caminhão não encontrada"));

        BalancaResponseDto balanca = balancaService.getBalancaById(pesoEstabilizado.idBalanca())
                .orElseThrow(() -> new RuntimeException("Balança não encontrada"));

        PesagemEntity consolidada = getPesagemEntity(pesoEstabilizado, demanda, caminhao, balanca);

        GraoResponseDto grao = graoService.getGraoById(consolidada.getIdGrao())
                .orElseThrow(() -> new RuntimeException("Grão não encontrada"));

        consolidada.setCusto(calculaPrecoCusto(grao.getPrecoCompraPorTonelada(), consolidada.getPesoLiquido()));

        PesagemEntity saved = pesagemRepository.save(consolidada);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_BALANCA,
                RabbitMQConfig.ROUTING_KEY_CONCLUIDA,
                saved
        );
    }

    @NotNull
    private static PesagemEntity getPesagemEntity(PesoEstabilizadoDTO pesoEstabilizado, DemandaTransporteEntity demanda,
                                                  CaminhaoResponseDto caminhao, BalancaResponseDto balanca) {
        PesagemEntity consolidada = new PesagemEntity();
        consolidada.setIdBalanca(pesoEstabilizado.idBalanca());
        consolidada.setIdDemanda(demanda.getId());
        consolidada.setIdCaminhao(caminhao.getId());
        consolidada.setIdGrao(demanda.getIdGrao());
        consolidada.setIdFilial(demanda.getIdFilial());
        consolidada.setIdDoca(balanca.getDocaId());

        BigDecimal tara = caminhao.getTara();
        consolidada.setPesoBruto(pesoEstabilizado.pesoBruto());
        consolidada.setTara(tara);
        consolidada.setPesoLiquido(pesoEstabilizado.pesoBruto().subtract(tara));

        consolidada.setCreatedBy(String.valueOf(pesoEstabilizado.idBalanca()));
        return consolidada;
    }

    @NotNull
    private static BigDecimal calculaPrecoCusto(BigDecimal precoCompraPorTonelada, BigDecimal pesoLiquidoKg) {

        if (pesoLiquidoKg.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Peso líquido inválido: deve ser maior que zero");
            return BigDecimal.ZERO;
        }

        if (precoCompraPorTonelada.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Preço por tonelada inválido: deve ser maior que zero");
        }

        BigDecimal pesoToneladas = pesoLiquidoKg.divide(BigDecimal.valueOf(1000), 4, RoundingMode.HALF_UP);
        return precoCompraPorTonelada.multiply(pesoToneladas);
    }
}
