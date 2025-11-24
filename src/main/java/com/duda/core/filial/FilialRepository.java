package com.serasa.core.filial;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface FilialRepository extends JpaRepository<FilialEntity, Long> {

    boolean existsByCnpj(String cnpj);

}