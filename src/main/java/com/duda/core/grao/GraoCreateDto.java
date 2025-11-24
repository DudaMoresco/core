package com.serasa.core.grao;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GraoCreateDto {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull(message = "Preço de compra por tonelada é obrigatório")
    @Positive(message = "Preço de compra por tonelada deve ser positivo")
    @Column(name = "preco_compra_por_tonelada", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoCompraPorTonelada;

}
