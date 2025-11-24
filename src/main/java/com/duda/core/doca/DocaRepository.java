package com.duda.core.doca;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocaRepository extends JpaRepository<DocaEntity, Long> {
}