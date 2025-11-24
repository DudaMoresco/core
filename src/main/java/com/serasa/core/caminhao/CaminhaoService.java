package com.serasa.core.caminhao;

import com.serasa.core.filial.FilialResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CaminhaoService {

    private final CaminhaoRepository caminhaoRepository;

    @Autowired
    public CaminhaoService(CaminhaoRepository caminhaoRepository) {this.caminhaoRepository = caminhaoRepository;}

    public Long createCaminhao(CaminhaoCreateDto newCaminhao) {
        if(caminhaoRepository.existsByPlaca(newCaminhao.getPlaca())) {
            throw new IllegalArgumentException("Já existe um caminhao cadastrado com a Placa informada.");
        }

        CaminhaoEntity caminhao = new CaminhaoEntity(
                newCaminhao.getPlaca(),
                newCaminhao.getTara(),
                "sistema"
        );

        CaminhaoEntity savedCaminhao = caminhaoRepository.save(caminhao);

        return savedCaminhao.getId();
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
