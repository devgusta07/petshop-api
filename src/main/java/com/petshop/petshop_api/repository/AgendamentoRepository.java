package com.petshop.petshop_api.repository;

import com.petshop.petshop_api.model.Agendamento;
import com.petshop.petshop_api.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {


    List<Agendamento> findByData( LocalDate data);

    List<Agendamento> findByTutorIdOrderByDataAscHorarioAsc(Integer idTutor);

    Optional<Agendamento> findByIdAndTutorId(Integer id_agendamento, Integer id_tutor);
}
