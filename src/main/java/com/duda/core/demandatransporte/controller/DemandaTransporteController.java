package com.duda.core.demandatransporte.controller;

import com.duda.core.demandatransporte.DemandaTransporteService;
import com.duda.core.demandatransporte.dto.DemandaTransporteCreateDTO;
import com.duda.core.demandatransporte.dto.DemandaTransporteResponseDto;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/demandas-transporte")
@Tag(name = "Demandas de Transporte", description = "Operações relacionadas à gestão do fluxo de demandas de transporte")
public class DemandaTransporteController {

    private final DemandaTransporteService demandaTransporteService;

    @Autowired
    public DemandaTransporteController(DemandaTransporteService demandaTransporteService) {
        this.demandaTransporteService = demandaTransporteService;
    }

    @Operation(
            summary = "Criar nova demanda de transporte",
            description = "Cria uma demanda de transporte a partir de caminhão, grão e filial informados.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Demanda criada com sucesso",
                            content = @Content(schema = @Schema(implementation = DemandaTransporteResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            }
    )
    @PostMapping
    public ResponseEntity<DemandaTransporteResponseDto> createDemandaTransporte(@Valid @RequestBody DemandaTransporteCreateDTO dto) {
        DemandaTransporteResponseDto created = demandaTransporteService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
            summary = "Listar todas as demandas de transporte",
            description = "Retorna a lista completa de todas as demandas cadastradas.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                            content = @Content(schema = @Schema(implementation = DemandaTransporteResponseDto.class))),
            }
    )
    @GetMapping
    public ResponseEntity<List<DemandaTransporteResponseDto>> getAllDemandas() {
        List<DemandaTransporteResponseDto> balancas = demandaTransporteService.getAllDemandas();
        return ResponseEntity.ok(balancas);
    }

    @Operation(
            summary = "Buscar demanda por ID",
            description = "Consulta os dados de uma demanda específica informando seu ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Demanda encontrada",
                            content = @Content(schema = @Schema(implementation = DemandaTransporteResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Demanda não encontrada", content = @Content),
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<DemandaTransporteResponseDto> getDemandaById(@PathVariable Long id) {
        Optional<DemandaTransporteResponseDto> balanca = demandaTransporteService.getDemandaById(id);
        return balanca.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Iniciar demanda de transporte",
            description = "Altera o status da demanda de PROGRAMADA para EM_ANDAMENTO.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Demanda iniciada com sucesso",
                            content = @Content(schema = @Schema(implementation = DemandaTransporteResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Status inválido ou operação proibida", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Demanda não encontrada", content = @Content),
            }
    )
    @PatchMapping("/{id}/start")
    public ResponseEntity<DemandaTransporteResponseDto> startDemandaTransporte(@PathVariable Long id) {
        DemandaTransporteResponseDto response = demandaTransporteService.iniciarDemandaTransporte(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Finalizar demanda de transporte",
            description = "Altera o status da demanda de EM_ANDAMENTO para FINALIZADA.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Demanda finalizada com sucesso",
                            content = @Content(schema = @Schema(implementation = DemandaTransporteResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Status inválido ou operação proibida", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Demanda não encontrada", content = @Content),
            }
    )
    @PatchMapping("/{id}/finish")
    public ResponseEntity<DemandaTransporteResponseDto> finishDemandaTransporte(@PathVariable Long id) {
        DemandaTransporteResponseDto response = demandaTransporteService.finalizarDemandaTransporte(id);
        return ResponseEntity.ok(response);
    }
}