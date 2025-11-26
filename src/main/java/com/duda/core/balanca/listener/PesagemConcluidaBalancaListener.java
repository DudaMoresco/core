package com.duda.core.balanca.listener;

import com.duda.core.balanca.BalancaService;
import com.duda.core.config.RabbitMQConfig;
import com.duda.core.pesagem.entity.PesagemEntity;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PesagemConcluidaBalancaListener {

    private final BalancaService balancaService;

    @Autowired
    public PesagemConcluidaBalancaListener(BalancaService balancaService) {
        this.balancaService = balancaService;
    }

    @RabbitListener(queues = RabbitMQConfig.FILA_PESAGEM_CONCLUIDA)
    @Transactional
    public void processarPesagemConcluida(PesagemEntity pesagemConsolidada) {
        Long idBalanca = pesagemConsolidada.getIdBalanca();
        balancaService.liberarBalancaById(idBalanca);
    }
}
