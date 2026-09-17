package com.petshop.petshop_api.repository;

import com.petshop.petshop_api.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {


    List<Agendamento> findByData( LocalDate data);
}
