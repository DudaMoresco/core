package com.duda.core.estoque.listener;

import com.duda.core.config.RabbitMQConfig;
import com.duda.core.estoque.EstoqueDocaGraoService;
import com.duda.core.estoque.dto.EstoqueDocaGraoResponseDto;
import com.duda.core.pesagem.entity.PesagemEntity;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PesagemConcluidaEstoqueListener {

    private final EstoqueDocaGraoService estoqueDocaGraoService;

    @Autowired
    public PesagemConcluidaEstoqueListener(EstoqueDocaGraoService estoqueDocaGraoService) {
        this.estoqueDocaGraoService = estoqueDocaGraoService;
    }

    @RabbitListener(queues = RabbitMQConfig.FILA_PESAGEM_CONCLUIDA)
    @Transactional
    public void processarPesagemConcluida(PesagemEntity pesagemConsolidada) {
        BigDecimal pesoLiquido = pesagemConsolidada.getPesoLiquido();

        EstoqueDocaGraoResponseDto estoque = estoqueDocaGraoService.getEstoqueByDocaAndGrao(pesagemConsolidada.getIdDoca(),
                pesagemConsolidada.getIdGrao()).orElseThrow();
        estoqueDocaGraoService.removerEstoque(estoque.getId(), pesoLiquido);
    }
}
