package com.duda.core.caminhao;

import com.duda.core.caminhao.dto.CaminhaoCreateDto;
import com.duda.core.caminhao.dto.CaminhaoResponseDto;
import com.duda.core.caminhao.entity.CaminhaoEntity;
import com.duda.core.caminhao.repository.CaminhaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CaminhaoService {

    private final CaminhaoRepository caminhaoRepository;

    @Autowired
    public CaminhaoService(CaminhaoRepository caminhaoRepository) {this.caminhaoRepository = caminhaoRepository;}

    public CaminhaoResponseDto create(CaminhaoCreateDto newCaminhao) {
        log.info("Iniciando processo de criação de caminhão para placa={}", newCaminhao.getPlaca());

        if(caminhaoRepository.existsByPlaca(newCaminhao.getPlaca())) {
            log.error("Tentativa de criar caminhão duplicado.");
            log.warn("Placa duplicada={}", newCaminhao.getPlaca());
            throw new IllegalArgumentException("Já existe um caminhao cadastrado com a Placa informada.");
        }

        CaminhaoEntity caminhao = new CaminhaoEntity(
                newCaminhao.getPlaca(),
                newCaminhao.getTara(),
                "sistema"
        );

        CaminhaoEntity savedCaminhao = caminhaoRepository.save(caminhao);
        log.info("Caminhão criado com placa={}", savedCaminhao.getPlaca());

        return CaminhaoResponseDto.from(savedCaminhao);
    }

    public List<CaminhaoResponseDto> getAllCaminhoes() {
        return caminhaoRepository.findAll()
                .stream()
                .map(CaminhaoResponseDto::from)
                .collect(Collectors.toList());
    }

    public Optional<CaminhaoResponseDto> getCaminhaoById(Long id) {
        if(Objects.isNull(id)) {
            throw new IllegalArgumentException("Id caminhão não pode ser null.");
        }

        return caminhaoRepository.findById(id)
                .map(CaminhaoResponseDto::from);
    }

    public Optional<CaminhaoResponseDto> getCaminhaoByPlaca(String placa) {
        if(Objects.isNull(placa) || placa.isEmpty()) {
            throw new IllegalArgumentException("Placa caminhão não pode ser null ou empty.");
        }

        return caminhaoRepository.findByPlaca(placa)
                .map(CaminhaoResponseDto::from);
    }
}
