package com.duda.core.relatorios.controller;

import com.duda.core.relatorios.RelatorioService;
import com.duda.core.relatorios.dto.PesagemReportDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/v1/relatorios")
@Tag(name = "Relatórios", description = "Endpoints para geração de relatórios")
public class RelatorioController {

    private final RelatorioService relatorioService;

    @Autowired
    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @Operation(
            summary = "Relatório de pesagens com paginação",
            description = "Retorna relatório de pesagens com filtros opcionais e suporte a paginação"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
    })
    @GetMapping("/pesagens")
    public ResponseEntity<Page<PesagemReportDto>> getPesagensReportPaginado(
            @Parameter(description = "ID da filial para filtrar")
            @RequestParam(required = false) Long filialId,

            @Parameter(description = "ID do caminhão para filtrar")
            @RequestParam(required = false) Long caminhaoId,

            @Parameter(description = "ID do grão para filtrar")
            @RequestParam(required = false) Long graoId,

            @Parameter(description = "Data e hora inicial do período (ISO 8601)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,

            @Parameter(description = "Data e hora final do período (ISO 8601)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim,

            @PageableDefault(
                    size = 20,
                    sort = "dataPesagem",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {

        if (dataInicio != null && dataFim != null && dataInicio.isAfter(dataFim)) {
            return ResponseEntity.badRequest().build();
        }

        Page<PesagemReportDto> pesagens = relatorioService.getPesagensReportPaginado(
                filialId, caminhaoId, graoId, dataInicio, dataFim, pageable
        );

        return ResponseEntity.ok(pesagens);
    }
}