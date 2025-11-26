package com.duda.core.estoque.repository;

import com.duda.core.estoque.entity.EstoqueDocaGraoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstoqueDocaGraoRepository extends JpaRepository<EstoqueDocaGraoEntity, Long> {

    /**
     * Busca estoque por doca e grão
     */
    Optional<EstoqueDocaGraoEntity> findByDocaIdAndGraoId(Long docaId, Long graoId);

    /**
     * Busca todos os estoques de uma doca específica
     */
    List<EstoqueDocaGraoEntity> findByDocaId(Long docaId);

    /**
     * Busca todos os estoques de um grão específico
     */
    List<EstoqueDocaGraoEntity> findByGraoId(Long graoId);

    /**
     * Busca estoques que estão vazios (quantidade atual = 0)
     */
    @Query("SELECT e FROM EstoqueDocaGraoEntity e WHERE e.qtdAtual = 0")
    List<EstoqueDocaGraoEntity> findEmptyStocks();

    /**
     * Busca estoques que estão cheios (quantidade atual = quantidade máxima)
     */
    @Query("SELECT e FROM EstoqueDocaGraoEntity e WHERE e.qtdAtual = e.qtdMax")
    List<EstoqueDocaGraoEntity> findFullStocks();

    /**
     * Busca estoques com quantidade atual abaixo do limite especificado
     */
    @Query("SELECT e FROM EstoqueDocaGraoEntity e WHERE e.qtdAtual <= :limite")
    List<EstoqueDocaGraoEntity> findLowStocks(@Param("limite") Integer limite);

    /**
     * Busca estoques com espaço disponível maior ou igual ao especificado
     */
    @Query("SELECT e FROM EstoqueDocaGraoEntity e WHERE (e.qtdMax - e.qtdAtual) >= :espacoMinimo")
    List<EstoqueDocaGraoEntity> findStocksWithAvailableSpace(@Param("espacoMinimo") Integer espacoMinimo);

    /**
     * Verifica se já existe estoque para uma doca e grão específicos
     */
    boolean existsByDocaIdAndGraoId(Long docaId, Long graoId);
}