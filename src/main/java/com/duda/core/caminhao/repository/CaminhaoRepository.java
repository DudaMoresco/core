package com.duda.core.caminhao.repository;

import com.duda.core.caminhao.entity.CaminhaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CaminhaoRepository extends JpaRepository<CaminhaoEntity, Long> {

    Optional<CaminhaoEntity> findByPlaca(String placa);

    boolean existsByPlaca(String placa);

}
