package com.duda.core.balanca.controller;

import com.duda.core.balanca.dto.BalancaCreateDto;
import com.duda.core.balanca.dto.BalancaResponseDto;
import com.duda.core.balanca.BalancaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/v1/balancas")
@Tag(name = "Balanças", description = "Operações relacionadas às balanças do sistema")
public class BalancaController {

    private final BalancaService balancaService;

    @Autowired
    public BalancaController(BalancaService balancaService) {
        this.balancaService = balancaService;
    }

    @Operation(
            summary = "Criar balança",
            description = "Cria uma nova balança vinculada a uma doca existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Balança criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou doca não encontrada")
    })
    @PostMapping
    public ResponseEntity<BalancaResponseDto> createBalanca(@Valid @RequestBody BalancaCreateDto balancaCreateDto) {
        BalancaResponseDto created = balancaService.created(balancaCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
            summary = "Listar todas as balanças",
            description = "Retorna todas as balanças cadastradas no sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<BalancaResponseDto>> getAllBalancas() {
        List<BalancaResponseDto> balancas = balancaService.getAllBalancas();
        return ResponseEntity.ok(balancas);
    }

    @Operation(
            summary = "Buscar balança por ID",
            description = "Retorna a balança correspondente ao ID informado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Balança encontrada"),
            @ApiResponse(responseCode = "404", description = "Balança não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BalancaResponseDto> getBalancaById(@PathVariable Long id) {
        Optional<BalancaResponseDto> balanca = balancaService.getBalancaById(id);
        return balanca.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Ativar balança",
            description = "Ativa uma balança pelo seu ID, caso esteja em estado LIVRE."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Balança ativada com sucesso"),
            @ApiResponse(responseCode = "400", description = "ID inválido ou balança em estado não permitido"),
            @ApiResponse(responseCode = "404", description = "Balança não encontrada")
    })
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<?> ativarBalancaById(@PathVariable Long id) {
        balancaService.ativarBalancaById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Desativar balança",
            description = "Define a balança como LIVRE, caso esteja em estado ATIVA."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Balança desativada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Estado inválido para desativação"),
            @ApiResponse(responseCode = "404", description = "Balança não encontrada")
    })
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<?> desativarBalancaById(@PathVariable Long id) {
        balancaService.liberarBalancaById(id);
        return ResponseEntity.ok().build();
    }
}
