package com.petshop.petshop_api.controller;

import com.petshop.petshop_api.dto.request.agendamento.AgendamentoRequestDTO;
import com.petshop.petshop_api.dto.response.agendamento.AgendamentoResponseDTO;
import com.petshop.petshop_api.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendamento")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping("/realizarAgendamento")
    public ResponseEntity<AgendamentoResponseDTO> realizarAgendamento(
            @Valid @RequestBody AgendamentoRequestDTO agendamento,
            @AuthenticationPrincipal Jwt jwt){

        Integer idTutor = Integer.parseInt(jwt.getSubject());


        return ResponseEntity.status(HttpStatus.CREATED).body( agendamentoService.realizarAgendamento(
                agendamento, idTutor));
    }

    @GetMapping("/agendamentos")
    public ResponseEntity<List<AgendamentoResponseDTO>> agendamentos(
            @AuthenticationPrincipal Jwt jwt){

        Integer idTutor = Integer.parseInt(jwt.getSubject());

        return ResponseEntity.status(HttpStatus.OK).body(agendamentoService.agendamentos(idTutor));
    }

    @PutMapping("/updateAgendamento/{id}")
    public ResponseEntity<AgendamentoResponseDTO> updateAgendamento(
            @PathVariable Integer id,
            @Valid @RequestBody AgendamentoRequestDTO updateAgendamento,
            @AuthenticationPrincipal Jwt jwt){

        Integer idTutor = Integer.parseInt(jwt.getSubject());

        return ResponseEntity.ok(agendamentoService.update(id, updateAgendamento, idTutor));

    }

    @DeleteMapping("/removerAgendamento/{id}")
    public ResponseEntity<Void> removerAgendamento(
            @PathVariable Integer idAgendamento,
        @AuthenticationPrincipal Jwt jwt){

        Integer idTutor = Integer.parseInt(jwt.getSubject());

         agendamentoService.removerPorId(idAgendamento, idTutor);

         return ResponseEntity.noContent().build();
    }
}
