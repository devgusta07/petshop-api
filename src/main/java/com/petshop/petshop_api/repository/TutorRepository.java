package com.petshop.petshop_api.repository;

import com.petshop.petshop_api.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TutorRepository extends JpaRepository<Tutor, Integer> {

    boolean existsByCpf(String cpf);

    Optional<Tutor> findByCpf(String cpf);


}
