package com.duda.core.balanca;

import com.duda.core.doca.DocaEntity;
import com.duda.core.doca.DocaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BalancaService {

    private final BalancaRepository balancaRepository;
    private final DocaRepository docaRepository;

    @Autowired
    public BalancaService(BalancaRepository balancaRepository, DocaRepository docaRepository) {
        this.balancaRepository = balancaRepository;
        this.docaRepository = docaRepository;
    }

    public Long createBalanca(BalancaCreateDto newBalanca) {
        Optional<DocaEntity> doca = docaRepository.findById(newBalanca.getDocaId());
        if (doca.isEmpty()) {
            throw new IllegalArgumentException("Doca não encontrada com o ID informado.");
        }

        BalancaEntity balanca = new BalancaEntity(
                doca.get(),
                "sistema"
        );

        BalancaEntity savedBalanca = balancaRepository.save(balanca);

        return savedBalanca.getId();
    }

    public List<BalancaResponseDto> getAllBalancas() {
        return balancaRepository.findAll()
                .stream()
                .map(BalancaResponseDto::from)
                .collect(Collectors.toList());
    }

    public Optional<BalancaResponseDto> getBalancaById(Long id) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("ID da balança não pode ser null.");
        }

        return balancaRepository.findById(id)
                .map(BalancaResponseDto::from);
    }
}