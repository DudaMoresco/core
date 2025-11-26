package com.duda.core.relatorios.repository;

import com.duda.core.pesagem.entity.PesagemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ReportRepository extends JpaRepository<PesagemEntity, Long> {

    @Query("""
        SELECT p.id as pesagemId,
               f.nome as nomeFilial,
               f.cnpj as cnpjFilial,
               c.placa as placaCaminhao,
               g.nome as nomeGrao,
               p.pesoBruto as pesoBruto,
               p.tara as tara,
               p.pesoLiquido as pesoLiquido,
               p.custo as custo,
               p.createdAt as dataPesagem,
               p.createdBy as criadoPor
        FROM PesagemEntity p
        JOIN FilialEntity f ON p.idFilial = f.id
        JOIN CaminhaoEntity c ON p.idCaminhao = c.id
        JOIN GraoEntity g ON p.idGrao = g.id
        WHERE (:filialId IS NULL OR p.idFilial = :filialId)
          AND (:caminhaoId IS NULL OR p.idCaminhao = :caminhaoId)
          AND (:graoId IS NULL OR p.idGrao = :graoId)
          AND (:dataInicio IS NULL OR p.createdAt >= :dataInicio)
          AND (:dataFim IS NULL OR p.createdAt <= :dataFim)
        ORDER BY p.createdAt DESC
    """)
    Page<PesagemReportProjection> findPesagensReportData(
            @Param("filialId") Long filialId,
            @Param("caminhaoId") Long caminhaoId,
            @Param("graoId") Long graoId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim,
            Pageable pageable
    );
}
