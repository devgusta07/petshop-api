package com.petshop.petshop_api.controller;

import com.petshop.petshop_api.dto.request.agendamento.AgendamentoRequestDTO;
import com.petshop.petshop_api.dto.response.agendamento.AgendamentoResponseDTO;
import com.petshop.petshop_api.dto.response.agendamento.RemocaoAgendamento;
import com.petshop.petshop_api.model.Agendamento;
import com.petshop.petshop_api.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agendamento")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping("/salvarAgendamento")
    public AgendamentoResponseDTO salvarAgendamento(
            @Valid @RequestBody AgendamentoRequestDTO agendamento){

        return agendamentoService.realizarAgendamento(agendamento);

    }

    @GetMapping("/buscarAgendamento/{id}")
    public AgendamentoResponseDTO buscarAgendamento(
            @PathVariable Integer id){

        return agendamentoService.buscarPorId(id);

    }

    @PutMapping("/updateAgendamento/{id}")
    public AgendamentoResponseDTO updateAgendamento(
            @PathVariable Integer id,
            @Valid @RequestBody AgendamentoRequestDTO updateAgendamento){

        return agendamentoService.update(id, updateAgendamento);

    }

    @DeleteMapping("/removerAgendamento/{id}")
    public RemocaoAgendamento removerAgendamento(
            @PathVariable Integer id){

        return agendamentoService.removerPorId(id);

    }
}
