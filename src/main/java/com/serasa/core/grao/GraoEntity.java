package com.serasa.core.grao;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "grao")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GraoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull(message = "Preço de compra por tonelada é obrigatório")
    @Positive(message = "Preço de compra por tonelada deve ser positivo")
    @Column(name = "preco_compra_por_tonelada", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoCompraPorTonelada;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", length = 255, updatable = false, nullable = false)
    private String createdBy;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", length = 255, nullable = false)
    private String updatedBy;

    public GraoEntity(String nome, BigDecimal precoCompraPorTonelada, String createdBy) {
        this.nome = nome;
        this.precoCompraPorTonelada = precoCompraPorTonelada;
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraoEntity grao = (GraoEntity) o;
        return Objects.equals(id, grao.id) && Objects.equals(nome, grao.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }

    @Override
    public String toString() {
        return "Grao{" +
                "id=" + id +
                '}';
    }
}
