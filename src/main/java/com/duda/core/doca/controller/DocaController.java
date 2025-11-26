package com.duda.core.doca.controller;

import com.duda.core.doca.dto.DocaResponseDto;
import com.duda.core.doca.DocaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/docas")
@CrossOrigin(origins = "*")
@Tag(
        name = "Docas",
        description = "Endpoints responsáveis pelo gerenciamento das docas do sistema"
)
public class DocaController {

    private final DocaService docaService;

    @Autowired
    public DocaController(DocaService docaService) {
        this.docaService = docaService;
    }


    @Operation(
            summary = "Criar uma nova doca",
            description = "Cria um novo registro de doca no sistema. ",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Doca criada com sucesso",
                            content = @Content(schema = @Schema(implementation = DocaResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno ao criar doca"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<DocaResponseDto> createDoca() {
        DocaResponseDto doca = docaService.create();
        return ResponseEntity.status(HttpStatus.CREATED).body(doca);
    }

    @Operation(
            summary = "Listar todas as docas",
            description = "Retorna uma lista completa contendo todas as docas cadastradas.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(schema = @Schema(implementation = DocaResponseDto.class))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<DocaResponseDto>> getAllDocas() {
        List<DocaResponseDto> docas = docaService.getAllDocas();
        return ResponseEntity.ok(docas);
    }

    @Operation(
            summary = "Buscar doca pelo ID",
            description = "Retorna os detalhes de uma doca específica utilizando seu ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Doca encontrada",
                            content = @Content(schema = @Schema(implementation = DocaResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Doca não encontrada"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<DocaResponseDto> getDocaById(@PathVariable Long id) {
        Optional<DocaResponseDto> doca = docaService.getDocaById(id);
        return doca.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}