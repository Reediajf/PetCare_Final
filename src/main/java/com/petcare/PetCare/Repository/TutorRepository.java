package com.petcare.PetCare.Repository;

import com.petcare.PetCare.Model.Tutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TutorRepository extends CrudRepository<Tutor,Long> {
    Optional<Tutor> findByEmail(String email);


    @Query("SELECT t.email FROM Tutor t WHERE t.id = :id")
    Optional<String> findEmailById(@Param("id") Long id);

}
