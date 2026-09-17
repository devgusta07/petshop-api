package com.petshop.petshop_api.repository;

import com.petshop.petshop_api.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, Integer> {

    boolean existsByTutorId(Integer id);

}
