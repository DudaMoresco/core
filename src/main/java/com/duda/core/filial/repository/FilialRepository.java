package com.duda.core.filial.repository;

import com.duda.core.filial.entity.FilialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface FilialRepository extends JpaRepository<FilialEntity, Long> {

    boolean existsByCnpj(String cnpj);

}