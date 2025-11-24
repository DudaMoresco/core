package com.serasa.core.filial;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FilialService {

    private final FilialRepository filialRepository;

    @Autowired
    public FilialService(FilialRepository filialRepository) {
        this.filialRepository = filialRepository;
    }


    public Long createFilial(FilialCreateDto newFilial) {

        if (filialRepository.existsByCnpj(newFilial.getCnpj())) {
            throw new IllegalArgumentException("Já existe uma filial cadastrada com o CNPJ informado.");
        }

        FilialEntity filial = new FilialEntity(
                newFilial.getNome(),
                newFilial.getCnpj(),
                "sistema");

        FilialEntity savedFilial = filialRepository.save(filial);

        return savedFilial.getId();
    }

    public List<FilialResponseDto> getAllFiliais() {
        return filialRepository.findAll()
                .stream()
                .map(FilialResponseDto::from)
                .collect(Collectors.toList());
    }

    public Optional<FilialResponseDto> getFilialById(Long id) {
        return filialRepository.findById(id)
                .map(FilialResponseDto::from);
    }
}
