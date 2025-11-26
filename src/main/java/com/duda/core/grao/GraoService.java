package com.duda.core.grao;

import com.duda.core.grao.dto.GraoCreateDto;
import com.duda.core.grao.dto.GraoResponseDto;
import com.duda.core.grao.entity.GraoEntity;
import com.duda.core.grao.repository.GraoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GraoService {

    private final GraoRepository graoRepository;

    @Autowired
    public GraoService( GraoRepository graoRepository) {
        this.graoRepository = graoRepository;
    }

    public GraoResponseDto create(GraoCreateDto newGrao) {
        GraoEntity grao = new GraoEntity(
                newGrao.getNome(),
                newGrao.getPrecoCompraPorTonelada(),
                "sistema"
        );

        GraoEntity savedGrao = graoRepository.save(grao);

        return GraoResponseDto.from(savedGrao);
    }

    public List<GraoResponseDto> getAllGraos() {
        return graoRepository.findAll()
                .stream()
                .map(GraoResponseDto::from)
                .collect(Collectors.toList());
    }

    public Optional<GraoResponseDto> getGraoById(Long id) {
        return graoRepository.findById(id)
                .map(GraoResponseDto::from);
    }
}
