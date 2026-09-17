package com.petshop.petshop_api.controller;

import com.petshop.petshop_api.dto.request.animal.AnimalRequestDTO;
import com.petshop.petshop_api.dto.response.animal.AnimalResponseDTO;
import com.petshop.petshop_api.dto.response.animal.RemocaoAnimal;
import com.petshop.petshop_api.dto.response.tutor.TutorResponseDTO;
import com.petshop.petshop_api.model.Animal;
import com.petshop.petshop_api.model.Tutor;
import com.petshop.petshop_api.service.AnimalService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/animais")
public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @PostMapping("/salvarAnimal")
    public AnimalResponseDTO salvarAnimal(
            @Valid @RequestBody AnimalRequestDTO dto){

        return animalService.salvarAnimal(dto);
    }

    @GetMapping("/buscarAnimal/{id}")
    public AnimalResponseDTO buscarAnimal(
            @PathVariable Integer id){

        return animalService.buscarAnimalPorId(id);

    }

    @DeleteMapping("/removerAnimal/{id}")
    public RemocaoAnimal removerAnimal(
            @PathVariable Integer id){

        return animalService.removerAnimal(id);

    }

    @PutMapping("/update/{id}")
    public AnimalResponseDTO updateAnimal(
            @PathVariable Integer id,
            @RequestBody AnimalRequestDTO updateAnimal){

        return animalService.updateAnimal(id, updateAnimal);

    }

    @GetMapping("/buscarTutor/{id}")
    public TutorResponseDTO buscarTutor(
            @PathVariable Integer id){

        return animalService.buscarTutorAnimal(id);

    }
}
