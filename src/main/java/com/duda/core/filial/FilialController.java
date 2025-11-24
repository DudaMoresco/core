package com.duda.core.filial;

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
@RequestMapping("/api/filiais")
@CrossOrigin(origins = "*")
public class FilialController {

    private final FilialService filialService;

    @Autowired
    public FilialController(FilialService filialService) {
        this.filialService = filialService;
    }

    @PostMapping
    public ResponseEntity<?> createFilial(@Valid @RequestBody FilialCreateDto filialCreateDto) {
        try {
            Long id = filialService.createFilial(filialCreateDto);

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
    public ResponseEntity<List<FilialResponseDto>> getAllFiliais() {
        List<FilialResponseDto> filiais = filialService.getAllFiliais();
        return ResponseEntity.ok(filiais);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilialResponseDto> getFilialById(@PathVariable Long id) {
        Optional<FilialResponseDto> filial = filialService.getFilialById(id);
        return filial.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
