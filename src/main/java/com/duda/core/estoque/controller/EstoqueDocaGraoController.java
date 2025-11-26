package com.duda.core.estoque.controller;

import com.duda.core.estoque.EstoqueDocaGraoService;
import com.duda.core.estoque.dto.EstoqueDocaGraoCreateDto;
import com.duda.core.estoque.dto.EstoqueDocaGraoResponseDto;
import com.duda.core.estoque.dto.EstoqueDocaGraoUpdateDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/estoques")
@Tag(name = "Estoque Doca Grão", description = "Gerencia o estoque de grãos nas docas")
public class EstoqueDocaGraoController {

    private final EstoqueDocaGraoService estoqueService;

    @Autowired
    public EstoqueDocaGraoController(EstoqueDocaGraoService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @PostMapping
    @Operation(summary = "Criar novo estoque", description = "Cria um novo registro de estoque de grão em uma doca")
    public ResponseEntity<Map<String, Long>> createEstoque(@Valid @RequestBody EstoqueDocaGraoCreateDto createDto) {
        Long id = estoqueService.createEstoque(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", id));
    }

    @GetMapping
    @Operation(summary = "Listar todos os estoques", description = "Retorna todos os registros de estoque")
    public ResponseEntity<List<EstoqueDocaGraoResponseDto>> getAllEstoques() {
        return ResponseEntity.ok(estoqueService.getAllEstoques());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter estoque por ID", description = "Retorna o estoque correspondente ao ID informado")
    public ResponseEntity<EstoqueDocaGraoResponseDto> getEstoqueById(@PathVariable Long id) {
        Optional<EstoqueDocaGraoResponseDto> estoque = estoqueService.getEstoqueById(id);
        return estoque.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/doca/{docaId}")
    @Operation(summary = "Obter estoques por doca", description = "Retorna todos os estoques de uma doca específica")
    public ResponseEntity<List<EstoqueDocaGraoResponseDto>> getEstoquesByDoca(@PathVariable Long docaId) {
        return ResponseEntity.ok(estoqueService.getEstoquesByDocaId(docaId));
    }

    @GetMapping("/grao/{graoId}")
    @Operation(summary = "Obter estoques por grão", description = "Retorna todos os estoques de um grão específico")
    public ResponseEntity<List<EstoqueDocaGraoResponseDto>> getEstoquesByGrao(@PathVariable Long graoId) {
        return ResponseEntity.ok(estoqueService.getEstoquesByGraoId(graoId));
    }

    @GetMapping("/doca/{docaId}/grao/{graoId}")
    @Operation(summary = "Obter estoque por doca e grão", description = "Retorna o estoque específico de um grão em uma doca")
    public ResponseEntity<EstoqueDocaGraoResponseDto> getEstoqueByDocaAndGrao(
            @PathVariable Long docaId, @PathVariable Long graoId) {
        Optional<EstoqueDocaGraoResponseDto> estoque = estoqueService.getEstoqueByDocaAndGrao(docaId, graoId);
        return estoque.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar estoque", description = "Atualiza todas as quantidades de um estoque existente")
    public ResponseEntity<Map<String, String>> updateEstoque(@PathVariable Long id,
                                                             @Valid @RequestBody EstoqueDocaGraoUpdateDto updateDto) {
        estoqueService.updateEstoqueQuantidades(id, updateDto);
        return ResponseEntity.ok(Map.of("message", "Estoque atualizado com sucesso"));
    }

    @PatchMapping("/{id}/quantidade-atual")
    @Operation(summary = "Atualizar quantidade atual", description = "Atualiza apenas a quantidade atual do estoque")
    public ResponseEntity<Map<String, String>> updateQuantidadeAtual(@PathVariable Long id,
                                                                     @RequestParam BigDecimal quantidade) {
        estoqueService.updateQuantidadeAtual(id, quantidade);
        return ResponseEntity.ok(Map.of("message", "Quantidade atual atualizada com sucesso"));
    }

    @PatchMapping("/{id}/quantidade-maxima")
    @Operation(summary = "Atualizar quantidade máxima", description = "Atualiza apenas a quantidade máxima do estoque")
    public ResponseEntity<Map<String, String>> updateQuantidadeMaxima(@PathVariable Long id,
                                                                      @RequestParam BigDecimal quantidade) {
        estoqueService.updateQuantidadeMaxima(id, quantidade);
        return ResponseEntity.ok(Map.of("message", "Quantidade máxima atualizada com sucesso"));
    }

    @PostMapping("/{id}/adicionar")
    @Operation(summary = "Adicionar estoque", description = "Adiciona uma quantidade ao estoque existente")
    public ResponseEntity<Map<String, String>> adicionarEstoque(@PathVariable Long id,
                                                                @RequestParam BigDecimal quantidade) {
        estoqueService.adicionarEstoque(id, quantidade);
        return ResponseEntity.ok(Map.of("message", "Estoque adicionado com sucesso"));
    }

    @PostMapping("/{id}/remover")
    @Operation(summary = "Remover estoque", description = "Remove uma quantidade do estoque existente")
    public ResponseEntity<Map<String, String>> removerEstoque(@PathVariable Long id,
                                                              @RequestParam BigDecimal quantidade) {
        estoqueService.removerEstoque(id, quantidade);
        return ResponseEntity.ok(Map.of("message", "Estoque removido com sucesso"));
    }
}
