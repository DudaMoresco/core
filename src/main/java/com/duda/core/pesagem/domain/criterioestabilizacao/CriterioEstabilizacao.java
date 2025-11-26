package com.duda.core.pesagem.domain.criterioestabilizacao;

import com.duda.core.pesagem.domain.EstadoBalanca;

public interface CriterioEstabilizacao {
    boolean avaliar(EstadoBalanca estado);
}