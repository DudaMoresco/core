package com.duda.core.demandatransporte.entity;

import com.duda.core.demandatransporte.enumeration.StatusDemandaTransporte;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "demanda_transporte")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DemandaTransporteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "ID do Caminhão é obrigatório")
    @Column(name = "id_caminhao", nullable = false)
    private Long idCaminhao;

    @NotNull(message = "ID do Grão é obrigatório")
    @Column(name = "id_grao", nullable = false)
    private Long idGrao;

    @NotNull(message = "ID da Filial é obrigatório")
    @Column(name = "id_filial", nullable = false)
    private Long idFilial;

    @NotNull(message = "Status é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusDemandaTransporte status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", updatable = false, nullable = false)
    private String createdBy;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    public DemandaTransporteEntity(Long idCaminhao, Long idGrao, Long idFilial, StatusDemandaTransporte status,String createdBy ) {
        this.idCaminhao = idCaminhao;
        this.idGrao = idGrao;
        this.idFilial = idFilial;
        this.status = status;
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DemandaTransporteEntity demanda = (DemandaTransporteEntity) o;
        return Objects.equals(id, demanda.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DemandaTransporteEntity{id=" + id + '}';
    }
}
