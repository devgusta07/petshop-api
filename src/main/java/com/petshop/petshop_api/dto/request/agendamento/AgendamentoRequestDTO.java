package com.petshop.petshop_api.dto.request.agendamento;

import com.petshop.petshop_api.model.TipoServico;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoRequestDTO(LocalDate data,
                                    LocalTime horario,
                                    TipoServico servico,
                                    Integer idAnimal) {
}
