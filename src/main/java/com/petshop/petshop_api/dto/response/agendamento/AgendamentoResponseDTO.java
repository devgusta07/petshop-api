package com.petshop.petshop_api.dto.response.agendamento;

import com.petshop.petshop_api.model.TipoServico;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoResponseDTO(Integer id,
                                     LocalDate data,
                                     LocalTime horario,
                                     TipoServico servico,
                                     Integer idAnimal) {
}
