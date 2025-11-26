package com.duda.core.pesagem.repository;

import com.duda.core.pesagem.entity.PesagemEntity;
import com.duda.core.relatorios.dto.PesagemReportDto;
import com.duda.core.relatorios.dto.CustoReportDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PesagemRepository extends JpaRepository<PesagemEntity, Long> {
}
