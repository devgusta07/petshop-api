package com.petshop.petshop_api.repository;

import com.petshop.petshop_api.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnimalRepository extends JpaRepository<Animal, Integer> {

    boolean existsByTutorId(Integer id);

    Optional<Animal> findByNomeAndTutorId(String nome, Integer id);

    Optional<Animal> findByIdAndTutorId(Integer id_animal, Integer id_tutor);

    Optional<Animal> findByIdAndTutorCpf(Integer id_animal, String cpf);

}
