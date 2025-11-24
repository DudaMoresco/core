package com.duda.core.balanca;

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
@RequestMapping("/api/balancas")
@CrossOrigin(origins = "*")
public class BalancaController {

    private final MedicaoBalancaService medicaoBalancaService;
    private final BalancaService balancaService;

    @Autowired
    public BalancaController(MedicaoBalancaService medicaoBalancaService, BalancaService balancaService) {
        this.medicaoBalancaService = medicaoBalancaService;
        this.balancaService = balancaService;
    }

    @PostMapping
    public ResponseEntity<?> createBalanca(@Valid @RequestBody BalancaCreateDto balancaCreateDto) {
        try {
            Long id = balancaService.createBalanca(balancaCreateDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Erro ao criar balança");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    @GetMapping
    public ResponseEntity<List<BalancaResponseDto>> getAllBalancas() {
        List<BalancaResponseDto> balancas = balancaService.getAllBalancas();
        return ResponseEntity.ok(balancas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BalancaResponseDto> getBalancaById(@PathVariable Long id) {
        Optional<BalancaResponseDto> balanca = balancaService.getBalancaById(id);
        return balanca.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/medicao")
    public ResponseEntity<?> createMedicao(@Valid @RequestBody MedicaoBalancaCreateDto medicaoBalancaCreateDto) {
        try {
            medicaoBalancaService.createMedicaoBalanca(medicaoBalancaCreateDto);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }
}
