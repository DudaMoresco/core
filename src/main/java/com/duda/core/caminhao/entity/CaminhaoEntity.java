package com.duda.core.caminhao.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "caminhao")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CaminhaoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Placa é obrigatória")
    @Pattern(regexp = "^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$|^[A-Z]{3}-[0-9]{4}$",
            message = "Placa deve estar no formato ABC1234 ou ABC-1234")
    @Column(name = "placa", nullable = false, unique = true, length = 8)
    private String placa;

    @NotNull(message = "Tara é obrigatória")
    @PositiveOrZero(message = "Tara deve ser positiva ou zero")
    @Column(name = "tara", nullable = false, precision = 10, scale = 3)
    private BigDecimal tara;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    public CaminhaoEntity(String placa, BigDecimal tara, String createdBy) {
        this.placa = placa;
        this.tara = tara;
        this.createdBy = createdBy;
        this.updatedBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CaminhaoEntity caminhao = (CaminhaoEntity) o;
        return Objects.equals(id, caminhao.id) && Objects.equals(placa, caminhao.placa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, placa);
    }

    @Override
    public String toString() {
        return "Caminhao{" +
                "id=" + id +
                ", placa='" + placa + '\'' +
                '}';
    }
}
