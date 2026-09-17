package com.petshop.petshop_api.controller;

import com.petshop.petshop_api.dto.response.animal.AnimalTutorResponseDTO;
import com.petshop.petshop_api.dto.response.tutor.RemocaoTutor;
import com.petshop.petshop_api.dto.request.tutor.TutorRequestDTO;
import com.petshop.petshop_api.dto.response.tutor.TutorResponseDTO;
import com.petshop.petshop_api.model.Animal;
import com.petshop.petshop_api.model.Tutor;
import com.petshop.petshop_api.service.TutorService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tutor")
public class TutorController {

    private final TutorService tutorService;

    public TutorController(TutorService tutorService) {
        this.tutorService = tutorService;
    }

    @PostMapping("/salvarTutor")
    public TutorResponseDTO salvarTutor(
            @Valid @RequestBody TutorRequestDTO dto){

        return tutorService.salvarTutor(dto);

    }

    @GetMapping("/buscarPorId/{id}")
    public TutorResponseDTO buscarTutorPorId(
            @PathVariable Integer id){

        return tutorService.buscarTutorPorId(id);

    }

    @GetMapping("/buscarPorContato")
    public TutorResponseDTO buscarTutorPorContato(
            @RequestParam String contato){

        return tutorService.buscarPorContato(contato);

    }

    @DeleteMapping("/removerTutor/{id}")
    public RemocaoTutor removerTutor(
            @PathVariable Integer id){

        return tutorService.removerPorId(id);

    }

    @PutMapping("/update/{id}")
    public TutorResponseDTO updateTutor(
            @PathVariable Integer id,
            @Valid @RequestBody TutorRequestDTO updateTutor){

        return tutorService.updateTutor(id, updateTutor);

    }

    @GetMapping("/animaisVinculados/{id}")
    public List<AnimalTutorResponseDTO> animaisVinculados(
            @PathVariable Integer id){

        return tutorService.animaisVinculados(id);

    }
}
