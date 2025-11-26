package com.duda.core.filial.controller;

import com.duda.core.filial.dto.FilialCreateDto;
import com.duda.core.filial.dto.FilialResponseDto;
import com.duda.core.filial.FilialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/filiais")
@Tag(name = "Filiais", description = "Endpoints para gerenciamento de filiais")
public class FilialController {

    private final FilialService filialService;

    @Autowired
    public FilialController(FilialService filialService) {
        this.filialService = filialService;
    }

    @Operation(
            summary = "Criar nova filial",
            description = "Cria uma nova filial com nome e CNPJ. Retorna a filial criada.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Filial criada com sucesso",
                            content = @Content(schema = @Schema(implementation = FilialResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos enviados pelo cliente"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<FilialResponseDto> createFilial(@Valid @RequestBody FilialCreateDto filialCreateDto) {
        FilialResponseDto created = filialService.create(filialCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
            summary = "Listar todas as filiais",
            description = "Retorna a lista completa de filiais cadastradas."
    )
    @GetMapping
    public ResponseEntity<List<FilialResponseDto>> getAllFiliais() {
        List<FilialResponseDto> filiais = filialService.getAllFiliais();
        return ResponseEntity.ok(filiais);
    }

    @Operation(
            summary = "Buscar filial por ID",
            description = "Retorna a filial correspondente ao ID informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Filial encontrada",
                            content = @Content(schema = @Schema(implementation = FilialResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Nenhuma filial encontrada com o ID informado"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<FilialResponseDto> getFilialById(@PathVariable Long id) {
        Optional<FilialResponseDto> filial = filialService.getFilialById(id);
        return filial.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
