package com.duda.core.balanca;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MedicaoBalancaRepository extends JpaRepository<MedicaoBalancaEntity, Long> {

    List<MedicaoBalancaEntity> findByIdBalancaAndCreatedAtAfter(Long idBalanca, LocalDateTime createdAt);

}
