package com.duda.core.estoque.entity;

import com.duda.core.doca.entity.DocaEntity;
import com.duda.core.grao.entity.GraoEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "estoque_doca_grao")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EstoqueDocaGraoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Doca é obrigatória")
    @ManyToOne
    @JoinColumn(name = "id_doca", nullable = false)
    private DocaEntity doca;

    @NotNull(message = "Grão é obrigatório")
    @ManyToOne
    @JoinColumn(name = "id_grao", nullable = false)
    private GraoEntity grao;

    @NotNull(message = "Quantidade máxima é obrigatória")
    @PositiveOrZero(message = "Quantidade máxima deve ser zero ou positiva")
    @Column(name = "qtd_max", nullable = false, precision = 10, scale = 3)
    private BigDecimal qtdMax;

    @NotNull(message = "Quantidade atual é obrigatória")
    @PositiveOrZero(message = "Quantidade atual deve ser zero ou positiva")
    @Column(name = "qtd_atual", nullable = false, precision = 10, scale = 3)
    private BigDecimal qtdAtual;

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

    public EstoqueDocaGraoEntity(DocaEntity doca, GraoEntity grao, BigDecimal qtdMax, BigDecimal qtdAtual, String createdBy) {
        this.doca = doca;
        this.grao = grao;
        this.qtdMax = qtdMax;
        this.qtdAtual = qtdAtual;
        this.createdBy = createdBy;
        this.updatedBy = createdBy;
    }

    public void atualizarQuantidadeAtual(BigDecimal novaQtdAtual, String atualizadoPor) {
        if (novaQtdAtual.compareTo(this.qtdMax) > 0) {
            throw new IllegalArgumentException("Quantidade atual não pode exceder a quantidade máxima");
        }
        this.qtdAtual = novaQtdAtual;
        this.updatedBy = atualizadoPor;
    }

    public void atualizarQuantidadeMaxima(BigDecimal novaQtdMax, String atualizadoPor) {
        if (novaQtdMax.compareTo(this.qtdAtual) < 0) {
            throw new IllegalArgumentException("Quantidade máxima não pode ser menor que a quantidade atual");
        }
        this.qtdMax = novaQtdMax;
        this.updatedBy = atualizadoPor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EstoqueDocaGraoEntity that = (EstoqueDocaGraoEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(doca, that.doca) && Objects.equals(grao, that.grao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, doca, grao);
    }

    @Override
    public String toString() {
        return "EstoqueDocaGraoEntity{" + "id=" + id + '}';
    }
}
