package com.duda.core.grao.controller;

import com.duda.core.grao.dto.GraoCreateDto;
import com.duda.core.grao.dto.GraoResponseDto;
import com.duda.core.grao.GraoService;
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
@RequestMapping("/v1/graos")
@Tag(name = "Grãos", description = "Endpoints para gerenciamento de tipos de grãos")
public class GraoController {

    private final GraoService graoService;

    @Autowired
    public GraoController(GraoService graoService) {
        this.graoService = graoService;
    }

    @Operation(
            summary = "Criar novo grão",
            description = "Cria um novo tipo de grão com nome e propriedades básicas.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Grão criado com sucesso",
                            content = @Content(schema = @Schema(implementation = GraoResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos")
            }
    )
    @PostMapping
    public ResponseEntity<GraoResponseDto> createGrao(@Valid @RequestBody GraoCreateDto graoCreateDto) {
        GraoResponseDto created = graoService.create(graoCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
            summary = "Listar todos os grãos",
            description = "Retorna a lista de todos os tipos de grãos cadastrados."
    )
    @GetMapping
    public ResponseEntity<List<GraoResponseDto>> getAllGraos() {
        List<GraoResponseDto> filiais = graoService.getAllGraos();
        return ResponseEntity.ok(filiais);
    }

    @Operation(
            summary = "Buscar grão por ID",
            description = "Retorna os dados do grão correspondente ao ID informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Grão encontrado",
                            content = @Content(schema = @Schema(implementation = GraoResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Grão não encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<GraoResponseDto> getGraoById(@PathVariable Long id) {
        Optional<GraoResponseDto> filial = graoService.getGraoById(id);
        return filial.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
