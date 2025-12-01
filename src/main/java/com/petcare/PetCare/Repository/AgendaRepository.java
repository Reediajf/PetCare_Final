package com.petcare.PetCare.Repository;

import com.petcare.PetCare.Model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    boolean existsByDataInicio(LocalDateTime dataInicio);

}
