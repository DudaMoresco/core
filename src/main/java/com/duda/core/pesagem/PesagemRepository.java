package com.duda.core.pesagem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PesagemRepository extends JpaRepository<PesagemEntity, Long> {
}
