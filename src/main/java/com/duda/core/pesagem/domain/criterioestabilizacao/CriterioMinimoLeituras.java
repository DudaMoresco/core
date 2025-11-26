package com.duda.core.pesagem.domain.criterioestabilizacao;

import com.duda.core.pesagem.domain.EstadoBalanca;
import org.springframework.stereotype.Component;

@Component
public class CriterioMinimoLeituras implements CriterioEstabilizacao {

    @Override
    public boolean avaliar(EstadoBalanca estado) {
        int minimoLeituras = 15;
        return estado.getLeituras().size() >= minimoLeituras;
    }

}