package com.serasa.core.demandatransporte;

import com.serasa.core.caminhao.CaminhaoResponseDto;
import com.serasa.core.caminhao.CaminhaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DemandaTransporteService {

    private final DemandaTransporteRepository demandaTransporteRepository;
    private final CaminhaoService caminhaoService;

    @Autowired
    public DemandaTransporteService(DemandaTransporteRepository demandaTransporteRepository, CaminhaoService caminhaoService) {
        this.demandaTransporteRepository = demandaTransporteRepository;
        this.caminhaoService = caminhaoService;
    }

    public DemandaTransporteEntity getByCaminhaoPlacaAndStatus(String placa, StatusDemandaTransporte status) {

        Optional<CaminhaoResponseDto> caminhao = caminhaoService.getCaminhaoByPlaca(placa);

        DemandaTransporteEntity demanda = demandaTransporteRepository.findByIdCaminhaoAndStatus(caminhao.get().getId(), status);

        return demanda;
    }


    public void finalizarPesagem(Long idDemanda){}

}
