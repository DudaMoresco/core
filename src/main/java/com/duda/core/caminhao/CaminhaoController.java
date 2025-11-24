package com.serasa.core.caminhao;

import com.serasa.core.filial.FilialCreateDto;
import com.serasa.core.filial.FilialResponseDto;
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
@RequestMapping("/api/caminhoes")
@CrossOrigin(origins = "*")
public class CaminhaoController {

    private final CaminhaoService caminhaoService;

    @Autowired
    public CaminhaoController(CaminhaoService caminhaoService){this.caminhaoService = caminhaoService;}

    @PostMapping
    public ResponseEntity<?> createCaminhao(@Valid @RequestBody CaminhaoCreateDto caminhaoCreateDto) {
        try {
            Long id = caminhaoService.createCaminhao(caminhaoCreateDto);

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
    public ResponseEntity<List<CaminhaoResponseDto>> getAllCaminhoes() {
        List<CaminhaoResponseDto> caminhoes = caminhaoService.getAllCaminhoes();
        return ResponseEntity.ok(caminhoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CaminhaoResponseDto> getCaminhaoById(@PathVariable Long id) {
        Optional<CaminhaoResponseDto> filial = caminhaoService.getCaminhaoById(id);
        return filial.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
