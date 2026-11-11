package com.petshop.petshop_api.controller;

import com.petshop.petshop_api.dto.response.animal.AnimalTutorResponseDTO;
import com.petshop.petshop_api.dto.response.tutor.RemocaoTutor;
import com.petshop.petshop_api.dto.response.tutor.TutorResponseDTO;
import com.petshop.petshop_api.service.TutorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final TutorService tutorService;

    public AdminController(TutorService tutorService) {
        this.tutorService = tutorService;
    }


    @GetMapping("/buscarPorCpf/{cpf}")
    public ResponseEntity<TutorResponseDTO> buscarTutorPorId(
            @PathVariable String cpf){

        return ResponseEntity.ok(tutorService.buscarTutorPorCpf(cpf));

    }

    @DeleteMapping("/removerTutor/{cpf}")
    public ResponseEntity<Void> removerTutor(
            @PathVariable String cpf){

        tutorService.removerPorCpf(cpf);

        return ResponseEntity.noContent().build();

    }

    @GetMapping("/animaisVinculados/{cpf}")
    public ResponseEntity<List<AnimalTutorResponseDTO>> animaisVinculados(
            @PathVariable String cpf){

        return ResponseEntity.ok(tutorService.animaisVinculados(cpf));

    }
}
