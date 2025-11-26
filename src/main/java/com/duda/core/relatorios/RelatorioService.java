package com.duda.core.relatorios;


import com.duda.core.relatorios.dto.PesagemReportDto;
import com.duda.core.relatorios.repository.PesagemReportProjection;
import com.duda.core.relatorios.repository.ReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class RelatorioService {

    @Autowired
    private ReportRepository reportRepository;

    @Transactional(readOnly = true)
    public Page<PesagemReportDto> getPesagensReportPaginado(
            Long filialId,
            Long caminhaoId,
            Long graoId,
            LocalDateTime dataInicio,
            LocalDateTime dataFim,
            Pageable pageable
    ) {
        log.debug("Buscando relatório de pesagens - Filial: {}, Caminhão: {}, Grão: {}, " +
                        "Período: {} a {}, Página: {}",
                filialId, caminhaoId, graoId, dataInicio, dataFim, pageable.getPageNumber());

        Page<PesagemReportProjection> projections = reportRepository.findPesagensReportData(
                filialId, caminhaoId, graoId, dataInicio, dataFim, pageable
        );

        return projections.map(PesagemReportDto::fromProjection);
    }
}