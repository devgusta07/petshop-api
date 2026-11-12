package com.petshop.petshop_api.controller;

import com.petshop.petshop_api.dto.request.agendamento.AgendamentoRequestDTO;
import com.petshop.petshop_api.dto.response.agendamento.AgendamentoResponseDTO;
import com.petshop.petshop_api.dto.response.animal.AnimalTutorResponseDTO;
import com.petshop.petshop_api.dto.response.tutor.TutorResponseDTO;
import com.petshop.petshop_api.service.AgendamentoService;
import com.petshop.petshop_api.service.TutorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final TutorService tutorService;
    private final AgendamentoService agendamentoService;

    public AdminController(TutorService tutorService,  AgendamentoService agendamentoService) {
        this.tutorService = tutorService;
        this.agendamentoService = agendamentoService;
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

    @GetMapping("/agendamentos/{cpf}")
    public ResponseEntity<List<AgendamentoResponseDTO>> agendamentos(
            @PathVariable String cpf
    ){

        return ResponseEntity.ok(agendamentoService.agendamentosAdmin(cpf));
    }

    @PutMapping("/update/{cpf}/{id}")
    public ResponseEntity<AgendamentoResponseDTO> update(@PathVariable String cpf,
                                                         @PathVariable Integer id,
                                                         @RequestBody AgendamentoRequestDTO dto){

        return ResponseEntity.ok(agendamentoService.updateAdmin(cpf, dto, id));

    }

    @DeleteMapping("/remover/{id}/{cpf}")
    public ResponseEntity<Void> remover(@PathVariable Integer id,
                                        @PathVariable String cpf){

        agendamentoService.removerPorCpfIdAdmin(id, cpf);
        return ResponseEntity.noContent().build();
    }
}
