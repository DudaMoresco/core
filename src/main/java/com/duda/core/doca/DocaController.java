package com.duda.core.doca;

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
@RequestMapping("/api/docas")
@CrossOrigin(origins = "*")
public class DocaController {

    private final DocaService docaService;

    @Autowired
    public DocaController(DocaService docaService) {
        this.docaService = docaService;
    }

    @PostMapping
    public ResponseEntity<?> createDoca() {
        try {
            Long id = docaService.createDoca();
            return ResponseEntity.status(HttpStatus.CREATED).body(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    @GetMapping
    public ResponseEntity<List<DocaResponseDto>> getAllDocas() {
        List<DocaResponseDto> docas = docaService.getAllDocas();
        return ResponseEntity.ok(docas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocaResponseDto> getDocaById(@PathVariable Long id) {
        Optional<DocaResponseDto> doca = docaService.getDocaById(id);
        return doca.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}