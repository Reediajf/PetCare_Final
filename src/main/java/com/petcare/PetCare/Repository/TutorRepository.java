package com.petcare.PetCare.Repository;

import com.petcare.PetCare.Model.Tutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TutorRepository extends CrudRepository<Tutor,Long> {
    Optional<Tutor> findByEmail(String email);
}
