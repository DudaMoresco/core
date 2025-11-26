package com.duda.core.balanca;

import com.duda.core.balanca.dto.BalancaCreateDto;
import com.duda.core.balanca.dto.BalancaResponseDto;
import com.duda.core.balanca.entity.BalancaEntity;
import com.duda.core.balanca.enumeration.StatusBalanca;
import com.duda.core.balanca.repository.BalancaRepository;
import com.duda.core.doca.entity.DocaEntity;
import com.duda.core.doca.repository.DocaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BalancaService {

    private final BalancaRepository balancaRepository;
    private final DocaRepository docaRepository;

    @Autowired
    public BalancaService(BalancaRepository balancaRepository, DocaRepository docaRepository) {
        this.balancaRepository = balancaRepository;
        this.docaRepository = docaRepository;
    }

    public BalancaResponseDto created(BalancaCreateDto newBalanca) {
        log.info("Criando balança para a doca ID={}", newBalanca.getDocaId());

        Optional<DocaEntity> doca = docaRepository.findById(newBalanca.getDocaId());
        if (doca.isEmpty()) {
            log.error("Falha ao criar balança: doca ID={} não encontrada", newBalanca.getDocaId());
            throw new IllegalArgumentException("Doca não encontrada com o ID informado.");
        }

        BalancaEntity balanca = new BalancaEntity(
                newBalanca.getDocaId(),
                StatusBalanca.LIVRE,
                "sistema"
        );

        BalancaEntity savedBalanca = balancaRepository.save(balanca);

        log.info("Balança criada com sucesso. ID={}", savedBalanca.getId());
        return BalancaResponseDto.from(savedBalanca);
    }

    public List<BalancaResponseDto> getAllBalancas() {
        log.info("Buscando todas as balanças cadastradas");
        return balancaRepository.findAll()
                .stream()
                .map(BalancaResponseDto::from)
                .collect(Collectors.toList());
    }

    public Optional<BalancaResponseDto> getBalancaById(Long id) {
        log.info("Buscando balança por ID={}", id);

        if (Objects.isNull(id)) {
            log.error("Falha na busca: ID informado é null");
            throw new IllegalArgumentException("ID da balança não pode ser null.");
        }

        return balancaRepository.findById(id)
                .map(BalancaResponseDto::from);
    }

    public void ativarBalancaById(Long id) {
        log.info("Ativando balança ID={}", id);

        if (Objects.isNull(id)) {
            log.error("Falha ao ativar: ID é null");
            throw new IllegalArgumentException("ID da balança não pode ser null.");
        }

        Optional<BalancaEntity> balanca = balancaRepository.findById(id);
        if (balanca.isEmpty()) {
            log.error("Falha ao ativar: balança ID={} não encontrada", id);
            throw new NoSuchElementException("ID da balança não corresponde a nenhuma balança cadastrada no sistema");
        }

        BalancaEntity balancaEntity = balanca.get();
        if (!balancaEntity.getStatus().equals(StatusBalanca.LIVRE)) {
            log.error("Falha ao ativar: balança ID={} não está LIVRE (status atual={})",
                    id, balancaEntity.getStatus());
            throw new IllegalStateException("Balança deve estar LIVRE para ser ATIVADA");
        }

        balancaEntity.setStatus(StatusBalanca.ATIVA);
        balancaRepository.save(balancaEntity);

        log.info("Balança ID={} ativada com sucesso", id);
    }

    public void liberarBalancaById(Long id) {
        log.info("Liberando balança ID={}", id);

        if (Objects.isNull(id)) {
            log.error("Falha ao liberar: ID é null");
            throw new IllegalArgumentException("ID da balança não pode ser null.");
        }

        Optional<BalancaEntity> balanca = balancaRepository.findById(id);
        if (balanca.isEmpty()) {
            log.error("Falha ao liberar: balança ID={} não encontrada", id);
            throw new NoSuchElementException("ID da balança não corresponde a nenhuma balança cadastrada no sistema");
        }

        BalancaEntity balancaEntity = balanca.get();
        if (!balancaEntity.getStatus().equals(StatusBalanca.ATIVA)) {
            log.error("Falha ao liberar: balança ID={} não está ATIVA (status atual={})",
                    id, balancaEntity.getStatus());
            throw new IllegalStateException("Balança deve estar ATIVA para ser LIVRE");
        }

        balancaEntity.setStatus(StatusBalanca.LIVRE);
        balancaRepository.save(balancaEntity);

        log.info("Balança ID={} liberada com sucesso", id);
    }
}
