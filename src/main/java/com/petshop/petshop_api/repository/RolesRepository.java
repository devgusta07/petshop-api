package com.petshop.petshop_api.repository;

import com.petshop.petshop_api.model.RoleName;
import com.petshop.petshop_api.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Integer> {

    Optional<Roles> findByName(RoleName name);

}
