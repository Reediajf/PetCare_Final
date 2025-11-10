package com.petcare.PetCare.Repository;

import com.petcare.PetCare.Model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalRepository extends JpaRepository<Animal,Long> {
    @Override
    List<Animal> findAll();

    @Override
    Optional<Animal> findById(Long aLong);


}
