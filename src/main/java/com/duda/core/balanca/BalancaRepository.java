package com.serasa.core.balanca;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalancaRepository extends JpaRepository<BalancaEntity, Long> {
}