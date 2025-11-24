package com.duda.core.demandatransporte;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandaTransporteRepository extends JpaRepository<DemandaTransporteEntity, Long> {

    DemandaTransporteEntity findByIdCaminhaoAndStatus(Long idCaminhao, StatusDemandaTransporte status);
}
