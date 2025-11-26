package com.duda.core.balanca.repository;

import com.duda.core.balanca.enumeration.StatusBalanca;
import com.duda.core.balanca.entity.BalancaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BalancaRepository extends JpaRepository<BalancaEntity, Long> {
    
    List<BalancaEntity> findByStatus(StatusBalanca status);
}