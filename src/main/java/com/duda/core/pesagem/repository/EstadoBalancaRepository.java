package com.duda.core.pesagem.repository;

import com.duda.core.pesagem.domain.EstadoBalanca;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EstadoBalancaRepository {
    private final Map<Long, EstadoBalanca> balancas = new ConcurrentHashMap<>();

    public Optional<EstadoBalanca> buscar(Long id) {
        return Optional.ofNullable(balancas.get(id));
    }

    public void salvar(Long id, EstadoBalanca estado) {
        balancas.put(id, estado);
    }
}
