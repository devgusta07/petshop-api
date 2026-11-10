package com.petshop.petshop_api.controller;

import com.petshop.petshop_api.dto.request.animal.AnimalRequestDTO;
import com.petshop.petshop_api.dto.response.animal.AnimalResponseDTO;
import com.petshop.petshop_api.service.AnimalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/animais")
public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @PostMapping("/salvarAnimal")
    public ResponseEntity<AnimalResponseDTO> salvarAnimal(
            @Valid @RequestBody AnimalRequestDTO dto, @AuthenticationPrincipal Jwt jwt){

        Integer id_Tutor = Integer.parseInt(jwt.getSubject());

        return ResponseEntity.status(HttpStatus.CREATED).body(animalService.salvarAnimal(dto, id_Tutor));
    }

    @GetMapping("/buscarAnimal")
    public ResponseEntity<AnimalResponseDTO> buscarAnimal(
            @RequestBody String nome, @AuthenticationPrincipal Jwt jwt){

        Integer id_Tutor = Integer.parseInt(jwt.getSubject());

        return ResponseEntity.ok(animalService.buscarAnimal(nome ,id_Tutor));

    }

    @DeleteMapping("/removerAnimal/{id}")
    public ResponseEntity<Void> removerAnimal(
            @PathVariable Integer id_animal, @AuthenticationPrincipal Jwt jwt){

        Integer id_Tutor = Integer.parseInt(jwt.getSubject());

         animalService.removerAnimal(id_animal, id_Tutor);

         return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AnimalResponseDTO> updateAnimal(
            @PathVariable Integer id_animal,
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody AnimalRequestDTO updateAnimal){

        Integer id_Tutor = Integer.parseInt(jwt.getSubject());

        return ResponseEntity.ok(animalService.updateAnimal(id_animal, id_Tutor, updateAnimal));

    }

}
