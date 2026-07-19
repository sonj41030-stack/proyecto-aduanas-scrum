package com.proyecto.aduanas.mspdi.repository;

import com.proyecto.aduanas.mspdi.entity.PersonaPdi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonaPdiRepository extends JpaRepository<PersonaPdi, Long> {
    Optional<PersonaPdi> findByDocumento(String documento);
}
