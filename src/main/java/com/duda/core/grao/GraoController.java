package com.duda.core.grao;

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
@RequestMapping("/api/graos")
@CrossOrigin(origins = "*")
public class GraoController {

    private final GraoService graoService;

    @Autowired
    public GraoController(GraoService graoService) {
        this.graoService = graoService;
    }

    @PostMapping
    public ResponseEntity<?> createGrao(@Valid @RequestBody GraoCreateDto graoCreateDto) {
        try {
            Long id = graoService.createGrao(graoCreateDto);

            //todo: retornar url do recurso criado

            return ResponseEntity.status(HttpStatus.CREATED).body(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Erro ao criar filial");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    @GetMapping
    public ResponseEntity<List<GraoResponseDto>> getAllGraos() {
        List<GraoResponseDto> filiais = graoService.getAllGraos();
        return ResponseEntity.ok(filiais);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GraoResponseDto> getGraoById(@PathVariable Long id) {
        Optional<GraoResponseDto> filial = graoService.getGraoById(id);
        return filial.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
