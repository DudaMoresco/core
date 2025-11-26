package com.duda.core.pesagem.queue;

import com.duda.core.config.RabbitMQConfig;
import com.duda.core.pesagem.EstabilizadorPesoService;
import com.duda.core.pesagem.domain.LeituraDto;
import com.duda.core.pesagem.domain.PesoEstabilizadoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LeiturasListener {
    private final Logger logger = LoggerFactory.getLogger(LeiturasListener.class);

    private final RabbitTemplate rabbitTemplate;
    private final EstabilizadorPesoService estabilizadorPesoService;

    @Autowired
    public LeiturasListener(RabbitTemplate rabbitTemplate, EstabilizadorPesoService estabilizadorPesoService) {
        this.rabbitTemplate = rabbitTemplate;
        this.estabilizadorPesoService = estabilizadorPesoService;
    }

    @RabbitListener(
            queues = RabbitMQConfig.FILA_LEITURAS_BALANCA,
            concurrency = "3-10"
    )
    public void processarLeitura(LeituraDto leitura) {
        logger.info("LEITURA CHEGOU");

        Optional<PesoEstabilizadoDTO> pesoEstabilizadoDTO = estabilizadorPesoService.processarLeitura(leitura);

        if(pesoEstabilizadoDTO.isPresent()) {

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_BALANCA,
                    RabbitMQConfig.ROUTING_KEY_ESTABILIZADA,
                    pesoEstabilizadoDTO
            );
        }
    }
}
