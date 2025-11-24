package com.duda.core.grao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GraoRepository extends JpaRepository<GraoEntity, Long> {

}
