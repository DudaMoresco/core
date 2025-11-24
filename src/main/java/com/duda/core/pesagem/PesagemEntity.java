package com.serasa.core.pesagem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pesagem")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PesagemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "ID da demanda é obrigatório")
    @Column(name = "id_demanda", nullable = false)
    private Long idDemanda;

    @NotNull(message = "ID da balança é obrigatório")
    @Column(name = "id_balanca", nullable = false)
    private Long idBalanca;

    @NotNull(message = "ID do Caminhão é obrigatório")
    @Column(name = "id_caminhao", nullable = false)
    private Long idCaminhao;

    @NotNull(message = "ID do Grão é obrigatório")
    @Column(name = "id_grao", nullable = false)
    private Long idGrao;

    @NotBlank(message = "ID da Filial é obrigatório")
    @Column(name = "id_filial", nullable = false)
    private Long idFilial;

    @NotNull(message = "Peso bruto é obrigatório")
    @Positive(message = "Peso bruto deve ser positivo")
    @Column(name = "peso_bruto", nullable = false, precision = 10, scale = 3)
    private BigDecimal pesoBruto;

    @NotNull(message = "Tara é obrigatório")
    @Positive(message = "Tara bruto deve ser positivo")
    @Column(name = "tara", nullable = false, precision = 10, scale = 3)
    private BigDecimal tara;

    //getPesoLiquido
    //getCusto

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", length = 255, updatable = false, nullable = false)
    private String createdBy;
}
