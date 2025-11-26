package com.duda.core.balanca.repository;

import com.duda.core.balanca.entity.BalancaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalancaRepository extends JpaRepository<BalancaEntity, Long> {
    
}