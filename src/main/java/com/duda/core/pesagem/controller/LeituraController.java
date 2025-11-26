package com.duda.core.pesagem.controller;

import com.duda.core.pesagem.queue.LeiturasProducer;
import com.duda.core.pesagem.domain.LeituraDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/leituras")
@Tag(name = "Leituras de Peso", description = "Endpoints para recebimento de leituras de balança")
public class LeituraController {

    private final LeiturasProducer leituraProducer;

    @Autowired
    public LeituraController(LeiturasProducer leituraProducer) {
        this.leituraProducer = leituraProducer;
    }

    @Operation(
            summary = "Receber nova leitura",
            description = "Recebe uma leitura de balança e envia para processamento assíncrono via Kafka/RabbitMQ"
    )
    @PostMapping()
    public ResponseEntity<?> receberLeitura(@Valid @RequestBody LeituraDto novaLeitura) {
        leituraProducer.enviarLeitura(novaLeitura);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}
