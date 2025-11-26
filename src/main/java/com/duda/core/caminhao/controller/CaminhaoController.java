package com.duda.core.caminhao.controller;

import com.duda.core.caminhao.dto.CaminhaoCreateDto;
import com.duda.core.caminhao.dto.CaminhaoResponseDto;
import com.duda.core.caminhao.CaminhaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/caminhoes")
@CrossOrigin(origins = "*")
@Tag(name = "Caminhões", description = "Endpoints para gestão de caminhões")
public class CaminhaoController {

    private final CaminhaoService caminhaoService;

    @Autowired
    public CaminhaoController(CaminhaoService caminhaoService){this.caminhaoService = caminhaoService;}

    @Operation(
            summary = "Criar um novo caminhão",
            description = "Registra um caminhão no sistema validando placa e tara.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Caminhão criado com sucesso",
                            content = @Content(schema = @Schema(implementation = CaminhaoResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "Caminhão já existe"),
                    @ApiResponse(responseCode = "400", description = "Erro de validação")
            }
    )
    @PostMapping
    public ResponseEntity<?> createCaminhao(@Valid @RequestBody CaminhaoCreateDto caminhaoCreateDto) {
        CaminhaoResponseDto created = caminhaoService.create(caminhaoCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
            summary = "Listar todos os caminhões",
            description = "Retorna todos os caminhões cadastrados no sistema.",
            responses = @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    )
    @GetMapping
    public ResponseEntity<List<CaminhaoResponseDto>> getAllCaminhoes() {
        List<CaminhaoResponseDto> caminhoes = caminhaoService.getAllCaminhoes();
        return ResponseEntity.ok(caminhoes);
    }

    @Operation(
            summary = "Buscar caminhão por ID",
            description = "Retorna os dados de um caminhão pelo seu ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Caminhão encontrado",
                            content = @Content(schema = @Schema(implementation = CaminhaoResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Caminhão não encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CaminhaoResponseDto> getCaminhaoById(@PathVariable Long id) {
        Optional<CaminhaoResponseDto> filial = caminhaoService.getCaminhaoById(id);
        return filial.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
