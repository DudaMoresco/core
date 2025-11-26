package com.duda.core.pesagem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
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

    @NotNull(message = "ID da Filial é obrigatório")
    @Column(name = "id_filial", nullable = false)
    private Long idFilial;

    @NotNull(message = "ID da Doca é obrigatório")
    @Column(name = "id_doca", nullable = false)
    private Long idDoca;

    @NotNull(message = "Peso bruto é obrigatório")
    @Positive(message = "Peso bruto deve ser positivo")
    @Column(name = "peso_bruto", nullable = false, precision = 10, scale = 3)
    private BigDecimal pesoBruto;

    @NotNull(message = "Tara é obrigatório")
    @Positive(message = "Tara deve ser positivo")
    @Column(name = "tara", nullable = false, precision = 10, scale = 3)
    private BigDecimal tara;

    @NotNull(message = "Peso líquido é obrigatório")
    @Positive(message = "Peso líquido deve ser positivo")
    @Column(name = "peso_liquido", nullable = false, precision = 10, scale = 3)
    private BigDecimal pesoLiquido;

    @NotNull(message = "Custo é obrigatório")
    @Positive(message = "Custo deve ser positivo")
    @Column(name = "custo", nullable = false, precision = 10, scale = 2)
    private BigDecimal custo;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", updatable = false, nullable = false)
    private String createdBy;
}
