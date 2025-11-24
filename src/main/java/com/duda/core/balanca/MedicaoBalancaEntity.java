package com.serasa.core.balanca;

import com.serasa.core.caminhao.CaminhaoEntity;
import com.serasa.core.doca.DocaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "medicao_balanca")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MedicaoBalancaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "ID da balança é obrigatório")
    @Column(name = "id_balanca", nullable = false)
    private Long idBalanca;

    @NotBlank(message = "Placa é obrigatória")
    @Pattern(regexp = "^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$|^[A-Z]{3}-[0-9]{4}$",
            message = "Placa deve estar no formato ABC1234 ou ABC-1234")
    @Column(name = "placa", nullable = false, unique = true, length = 8)
    private String placa;

    @NotNull(message = "Peso é obrigatório")
    @Positive(message = "Peso deve ser positivo")
    @Column(name = "peso", nullable = false, precision = 10, scale = 3)
    private BigDecimal peso;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", length = 255, updatable = false, nullable = false)
    private String createdBy;

    public MedicaoBalancaEntity(Long idBalanca, String placa, BigDecimal peso) {
        this.idBalanca = idBalanca;
        this.placa = placa;
        this.peso = peso;
        this.createdBy = String.valueOf(idBalanca);
    }
}
