package com.duda.core.estoque;

import com.duda.core.doca.entity.DocaEntity;
import com.duda.core.doca.repository.DocaRepository;
import com.duda.core.estoque.dto.EstoqueDocaGraoCreateDto;
import com.duda.core.estoque.dto.EstoqueDocaGraoResponseDto;
import com.duda.core.estoque.dto.EstoqueDocaGraoUpdateDto;
import com.duda.core.estoque.entity.EstoqueDocaGraoEntity;
import com.duda.core.estoque.repository.EstoqueDocaGraoRepository;
import com.duda.core.grao.entity.GraoEntity;
import com.duda.core.grao.repository.GraoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
@Service
public class EstoqueDocaGraoService {

    private static final Logger logger = LoggerFactory.getLogger(EstoqueDocaGraoService.class);

    private final EstoqueDocaGraoRepository estoqueRepository;
    private final DocaRepository docaRepository;
    private final GraoRepository graoRepository;

    @Autowired
    public EstoqueDocaGraoService(EstoqueDocaGraoRepository estoqueRepository,
                                  DocaRepository docaRepository,
                                  GraoRepository graoRepository) {
        this.estoqueRepository = estoqueRepository;
        this.docaRepository = docaRepository;
        this.graoRepository = graoRepository;
    }

    @Transactional
    public Long createEstoque(EstoqueDocaGraoCreateDto createDto) {
        logger.info("Criando estoque para doca {} e grão {}", createDto.getIdDoca(), createDto.getIdGrao());

        DocaEntity doca = docaRepository.findById(createDto.getIdDoca())
                .orElseThrow(() -> new IllegalArgumentException("Doca não encontrada com ID: " + createDto.getIdDoca()));

        GraoEntity grao = graoRepository.findById(createDto.getIdGrao())
                .orElseThrow(() -> new IllegalArgumentException("Grão não encontrado com ID: " + createDto.getIdGrao()));

        if (estoqueRepository.existsByDocaIdAndGraoId(createDto.getIdDoca(), createDto.getIdGrao())) {
            throw new IllegalArgumentException("Já existe estoque para esta doca e grão");
        }

        if (createDto.getQtdAtual().compareTo(createDto.getQtdMax()) > 0) {
            throw new IllegalArgumentException("Quantidade atual não pode exceder a quantidade máxima");
        }

        EstoqueDocaGraoEntity estoque = new EstoqueDocaGraoEntity(
                doca,
                grao,
                createDto.getQtdMax(),
                createDto.getQtdAtual(),
                "sistema"
        );

        EstoqueDocaGraoEntity savedEstoque = estoqueRepository.save(estoque);
        logger.info("Estoque criado com ID {}", savedEstoque.getId());
        return savedEstoque.getId();
    }

    public List<EstoqueDocaGraoResponseDto> getAllEstoques() {
        logger.info("Buscando todos os estoques");
        return estoqueRepository.findAll()
                .stream()
                .map(EstoqueDocaGraoResponseDto::from)
                .collect(Collectors.toList());
    }

    public Optional<EstoqueDocaGraoResponseDto> getEstoqueById(Long id) {
        logger.info("Buscando estoque com ID {}", id);
        return estoqueRepository.findById(id)
                .map(EstoqueDocaGraoResponseDto::from);
    }

    public List<EstoqueDocaGraoResponseDto> getEstoquesByDocaId(Long docaId) {
        logger.info("Buscando estoques para doca {}", docaId);
        return estoqueRepository.findByDocaId(docaId)
                .stream()
                .map(EstoqueDocaGraoResponseDto::from)
                .collect(Collectors.toList());
    }

    public List<EstoqueDocaGraoResponseDto> getEstoquesByGraoId(Long graoId) {
        logger.info("Buscando estoques para grão {}", graoId);
        return estoqueRepository.findByGraoId(graoId)
                .stream()
                .map(EstoqueDocaGraoResponseDto::from)
                .collect(Collectors.toList());
    }

    public Optional<EstoqueDocaGraoResponseDto> getEstoqueByDocaAndGrao(Long docaId, Long graoId) {
        logger.info("Buscando estoque para doca {} e grão {}", docaId, graoId);
        return estoqueRepository.findByDocaIdAndGraoId(docaId, graoId)
                .map(EstoqueDocaGraoResponseDto::from);
    }

    @Transactional
    public void updateEstoqueQuantidades(Long id, EstoqueDocaGraoUpdateDto updateDto) {
        logger.info("Atualizando quantidades do estoque {}", id);

        EstoqueDocaGraoEntity estoque = estoqueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Estoque não encontrado com ID: " + id));

        if (updateDto.getQtdMax() != null) {
            estoque.atualizarQuantidadeMaxima(updateDto.getQtdMax(), "sistema");
            logger.info("Quantidade máxima atualizada para {}", updateDto.getQtdMax());
        }

        if (updateDto.getQtdAtual() != null) {
            estoque.atualizarQuantidadeAtual(updateDto.getQtdAtual(), "sistema");
            logger.info("Quantidade atual atualizada para {}", updateDto.getQtdAtual());
        }

        estoqueRepository.save(estoque);
    }

    @Transactional
    public void updateQuantidadeAtual(Long id, BigDecimal novaQuantidade) {
        logger.info("Atualizando quantidade atual do estoque {} para {}", id, novaQuantidade);

        EstoqueDocaGraoEntity estoque = estoqueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Estoque não encontrado com ID: " + id));

        estoque.atualizarQuantidadeAtual(novaQuantidade, "sistema");
        estoqueRepository.save(estoque);
    }

    @Transactional
    public void updateQuantidadeMaxima(Long id, BigDecimal novaQuantidadeMaxima) {
        logger.info("Atualizando quantidade máxima do estoque {} para {}", id, novaQuantidadeMaxima);

        EstoqueDocaGraoEntity estoque = estoqueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Estoque não encontrado com ID: " + id));

        estoque.atualizarQuantidadeMaxima(novaQuantidadeMaxima, "sistema");
        estoqueRepository.save(estoque);
    }

    @Transactional
    public void adicionarEstoque(Long id, BigDecimal quantidade) {
        logger.info("Adicionando {} ao estoque {}", quantidade, id);

        EstoqueDocaGraoEntity estoque = estoqueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Estoque não encontrado com ID: " + id));

        BigDecimal novaQuantidade = estoque.getQtdAtual().add(quantidade);
        estoque.atualizarQuantidadeAtual(novaQuantidade, "sistema");
        estoqueRepository.save(estoque);
    }

    @Transactional
    public void removerEstoque(Long id, BigDecimal quantidade) {
        logger.info("Removendo {} do estoque {}", quantidade, id);

        EstoqueDocaGraoEntity estoque = estoqueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Estoque não encontrado com ID: " + id));

        BigDecimal novaQuantidade = estoque.getQtdAtual().subtract(quantidade);
        if (novaQuantidade.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Não é possível remover mais do que existe no estoque");
        }

        estoque.atualizarQuantidadeAtual(novaQuantidade, "sistema");
        estoqueRepository.save(estoque);
    }
}
