package com.duda.core.doca.repository;

import com.duda.core.doca.entity.DocaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocaRepository extends JpaRepository<DocaEntity, Long> {
}