package com.duda.core.doca;

import com.duda.core.doca.dto.DocaResponseDto;
import com.duda.core.doca.entity.DocaEntity;
import com.duda.core.doca.repository.DocaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocaService {

    private final DocaRepository docaRepository;

    @Autowired
    public DocaService(DocaRepository docaRepository) {
        this.docaRepository = docaRepository;
    }

    public DocaResponseDto create() {
        DocaEntity doca = new DocaEntity("sistema");
        DocaEntity savedDoca = docaRepository.save(doca);
        return DocaResponseDto.from(savedDoca);
    }

    public List<DocaResponseDto> getAllDocas() {
        return docaRepository.findAll()
                .stream()
                .map(DocaResponseDto::from)
                .collect(Collectors.toList());
    }

    public Optional<DocaResponseDto> getDocaById(Long id) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("ID da doca não pode ser null.");
        }

        return docaRepository.findById(id)
                .map(DocaResponseDto::from);
    }
}