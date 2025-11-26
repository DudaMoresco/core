package com.duda.core.demandatransporte;

import com.duda.core.caminhao.dto.CaminhaoResponseDto;
import com.duda.core.caminhao.CaminhaoService;
import com.duda.core.demandatransporte.dto.DemandaTransporteCreateDTO;
import com.duda.core.demandatransporte.dto.DemandaTransporteResponseDto;
import com.duda.core.demandatransporte.entity.DemandaTransporteEntity;
import com.duda.core.demandatransporte.enumeration.StatusDemandaTransporte;
import com.duda.core.demandatransporte.repository.DemandaTransporteRepository;
import com.duda.core.filial.FilialService;
import com.duda.core.grao.GraoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.duda.core.demandatransporte.enumeration.StatusDemandaTransporte.PROGRAMADA;

@Slf4j
@Service
public class DemandaTransporteService {

    private final DemandaTransporteRepository demandaTransporteRepository;
    private final CaminhaoService caminhaoService;
    private final GraoService graoService;
    private final FilialService filialService;

    @Autowired
    public DemandaTransporteService(DemandaTransporteRepository demandaTransporteRepository, CaminhaoService caminhaoService,
                                    GraoService graoService, FilialService filialService) {
        this.demandaTransporteRepository = demandaTransporteRepository;
        this.caminhaoService = caminhaoService;
        this.graoService = graoService;
        this.filialService = filialService;
    }

    public DemandaTransporteEntity getByCaminhaoPlacaAndStatus(String placa, StatusDemandaTransporte status) {
        log.info("Buscando demanda por placa={} e status={}", placa, status);

        CaminhaoResponseDto caminhao = caminhaoService.getCaminhaoByPlaca(placa)
                .orElseThrow(() -> new IllegalArgumentException("Caminhão não encontrado para a placa: " + placa));

        return demandaTransporteRepository.findByIdCaminhaoAndStatus(caminhao.getId(), status);
    }

    public DemandaTransporteResponseDto create(DemandaTransporteCreateDTO dto) {
        log.info("Criando nova demanda de transporte para caminhão={}, grao={}, filial={}",
                dto.getIdCaminhao(), dto.getIdGrao(), dto.getIdFilial());

        caminhaoService.getCaminhaoById(dto.getIdCaminhao())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Caminhão não encontrado para ID=" + dto.getIdCaminhao()
                ));

        graoService.getGraoById(dto.getIdGrao())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Grão não encontrado para ID=" + dto.getIdGrao()
                ));

        filialService.getFilialById(dto.getIdFilial())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Filial não encontrada para ID=" + dto.getIdFilial()
                ));

        DemandaTransporteEntity entity = new DemandaTransporteEntity(
                dto.getIdCaminhao(),
                dto.getIdGrao(),
                dto.getIdFilial(),
                PROGRAMADA,
                "sistema"
        );

        DemandaTransporteEntity saved = demandaTransporteRepository.save(entity);
        log.info("Demanda de transporte criada com sucesso id={}", saved.getId());

        return DemandaTransporteResponseDto.from(saved);
    }

    public List<DemandaTransporteResponseDto> getAllDemandas() {
        log.info("Buscando todas as demandas cadastradas");
        return demandaTransporteRepository.findAll()
                .stream()
                .map(DemandaTransporteResponseDto::from)
                .collect(Collectors.toList());
    }

    public Optional<DemandaTransporteResponseDto> getDemandaById(Long id) {
        log.info("Buscando demanda por ID={}", id);

        if (Objects.isNull(id)) {
            log.error("Falha na busca: ID informado é null");
            throw new IllegalArgumentException("ID da demanda não pode ser null.");
        }

        return demandaTransporteRepository.findById(id)
                .map(DemandaTransporteResponseDto::from);
    }

    public DemandaTransporteResponseDto iniciarDemandaTransporte(Long idDemanda) {
        log.info("Iniciando demanda de transporte id={}", idDemanda);

        DemandaTransporteEntity entity = demandaTransporteRepository.findById(idDemanda)
                .orElseThrow(() -> new IllegalArgumentException("Demanda de transporte não encontrada ID=" + idDemanda));

        if (entity.getStatus() != StatusDemandaTransporte.PROGRAMADA) {
            log.warn("Status inválido para demanda id={} esperado={}, atual={}",
                    entity.getId(), StatusDemandaTransporte.PROGRAMADA, entity.getStatus());
            throw new IllegalStateException("Demanda deve estar PROGRAMADA para ser iniciada");
        }

        entity.setStatus(StatusDemandaTransporte.EM_ANDAMENTO);
        entity.setUpdatedBy("sistema");

        DemandaTransporteEntity saved = demandaTransporteRepository.save(entity);
        log.info("Demanda id={} iniciada com sucesso", idDemanda);

        return DemandaTransporteResponseDto.from(saved);
    }

    public DemandaTransporteResponseDto finalizarDemandaTransporte(Long idDemanda) {
        log.info("Finalizando demanda de transporte id={}", idDemanda);

        DemandaTransporteEntity entity = demandaTransporteRepository.findById(idDemanda)
                .orElseThrow(() -> new IllegalArgumentException("Demanda de transporte não encontrada ID=" + idDemanda));

        if (entity.getStatus() != StatusDemandaTransporte.EM_ANDAMENTO) {
            log.warn("Status inválido para demanda id={} esperado={}, atual={}",
                    entity.getId(), StatusDemandaTransporte.EM_ANDAMENTO, entity.getStatus());
            throw new IllegalStateException("Demanda deve estar EM_ANDAMENTO para ser iniciada");
        }

        entity.setStatus(StatusDemandaTransporte.FINALIZADA);
        entity.setUpdatedBy("sistema");

        DemandaTransporteEntity saved = demandaTransporteRepository.save(entity);
        log.info("Demanda id={} finalizada com sucesso", idDemanda);

        return DemandaTransporteResponseDto.from(saved);
    }

}
