package com.duda.core.demandatransporte.repository;

import com.duda.core.demandatransporte.enumeration.StatusDemandaTransporte;
import com.duda.core.demandatransporte.entity.DemandaTransporteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandaTransporteRepository extends JpaRepository<DemandaTransporteEntity, Long> {

    DemandaTransporteEntity findByIdCaminhaoAndStatus(Long idCaminhao, StatusDemandaTransporte status);
}
