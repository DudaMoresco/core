package com.duda.core.pesagem.queue;

import com.duda.core.balanca.BalancaService;
import com.duda.core.balanca.dto.BalancaResponseDto;
import com.duda.core.caminhao.CaminhaoService;
import com.duda.core.caminhao.dto.CaminhaoResponseDto;
import com.duda.core.config.RabbitMQConfig;
import com.duda.core.pesagem.domain.LeituraDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class LeiturasProducer {

    private final Logger logger = LoggerFactory.getLogger(LeiturasProducer.class);
    private final RabbitTemplate rabbitTemplate;
    private final BalancaService balancaService;
    private final CaminhaoService caminhaoService;

    @Autowired
    public LeiturasProducer(RabbitTemplate rabbitTemplate,BalancaService balancaService,
                            CaminhaoService caminhaoService) {
        this.rabbitTemplate = rabbitTemplate;
        this.balancaService = balancaService;
        this.caminhaoService = caminhaoService;
    }

    public void enviarLeitura(LeituraDto novaLeitura) {

        Long idBalanca = novaLeitura.id();
        Optional<BalancaResponseDto> balanca = balancaService.getBalancaById(idBalanca);
        if(balanca.isEmpty()) {
            throw new NoSuchElementException("ID da balança não corresponde a nenhuma balança cadastrada no sistema");
        }

        String placaCaminhao = novaLeitura.plate();
        Optional<CaminhaoResponseDto> caminhao = caminhaoService.getCaminhaoByPlaca(placaCaminhao);
        if(caminhao.isEmpty()) {
            throw new NoSuchElementException("ID do caminhao não corresponde a nenhuma caminhão cadastrado no sistema");
        }

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_BALANCA,
                RabbitMQConfig.ROUTING_KEY_LEITURAS,
                novaLeitura
        );
    }

}
