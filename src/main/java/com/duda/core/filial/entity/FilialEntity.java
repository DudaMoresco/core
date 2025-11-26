package com.duda.core.filial.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "filial")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FilialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotBlank(message = "CNPJ é obrigatório")
    @Pattern(regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}",
            message = "CNPJ deve estar no formato XX.XXX.XXX/XXXX-XX")
    @Column(name = "cnpj", nullable = false, unique = true, length = 18)
    private String cnpj;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", updatable = false, nullable = false)
    private String createdBy;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "updated_by",nullable = false)
    private String updatedBy;

    public FilialEntity(String nome, String cnpj, String createdBy) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.createdBy = createdBy;
        this.updatedBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilialEntity filial = (FilialEntity) o;
        return Objects.equals(id, filial.getId()) && Objects.equals(cnpj, filial.getCnpj());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cnpj);
    }

    @Override
    public String toString() {
        return "FilialEntity{id=" + id + "}";
    }
}
