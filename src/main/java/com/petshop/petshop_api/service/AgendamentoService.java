package com.petshop.petshop_api.service;

import com.petshop.petshop_api.dto.request.agendamento.AgendamentoRequestDTO;
import com.petshop.petshop_api.dto.response.agendamento.AgendamentoResponseDTO;
import com.petshop.petshop_api.exception.RecursoJaExistenteException;
import com.petshop.petshop_api.exception.RecursoNaoEncontradoException;
import com.petshop.petshop_api.model.Agendamento;
import com.petshop.petshop_api.repository.AgendamentoRepository;
import com.petshop.petshop_api.repository.AnimalRepository;
import com.petshop.petshop_api.repository.TutorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final AnimalRepository animalRepository;
    private final TutorRepository tutorRepository;

    public AgendamentoService(AgendamentoRepository agendamentoRespository,
                              AnimalRepository animalRepository,
                              TutorRepository tutorRepository) {
        this.agendamentoRepository = agendamentoRespository;
        this.animalRepository = animalRepository;
        this.tutorRepository = tutorRepository;
    }

    private void verificarConflito(Agendamento agendamento){

        List<Agendamento> agendamentosDoDia =
                agendamentoRepository.findByData(agendamento.getData());

        LocalTime horarioInicio = agendamento.getHorario();
        LocalTime horarioFim = horarioInicio.plusHours(1);

        for (Agendamento agendamentoExistente : agendamentosDoDia) {

            LocalTime inicioExistente = agendamentoExistente.getHorario();
            LocalTime fimExistente = inicioExistente.plusHours(1);

            if (agendamentoExistente.getId().equals(agendamento.getId())) {
                continue;
            }

            if (horarioInicio.isBefore(fimExistente)
                    && horarioFim.isAfter(inicioExistente)) {

                throw new RecursoJaExistenteException(
                        "Ja existe um agendamento nessa faixa de horario"
                );
            }
        }

    }


    public AgendamentoResponseDTO realizarAgendamento(AgendamentoRequestDTO agendamento, Integer idTutor) {

        var animal = animalRepository.findByIdAndTutorId(agendamento.idAnimal(), idTutor)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Animal nao encontrado ou não pertence a esse tutor"
                        ));

        var tutor = tutorRepository.findById(idTutor)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Tutor não encontrado"
                        ));

        Agendamento novoAgendamento = new Agendamento();
        novoAgendamento.setData(agendamento.data());
        novoAgendamento.setHorario(agendamento.horario());
        novoAgendamento.setTipoServico(agendamento.servico());
        novoAgendamento.setAnimal(animal);
        novoAgendamento.setTutor(tutor);

       verificarConflito(novoAgendamento);

       agendamentoRepository.save(novoAgendamento);

       return new AgendamentoResponseDTO(
               novoAgendamento.getId(),
               novoAgendamento.getData(),
               novoAgendamento.getHorario(),
               novoAgendamento.getTipoServico(),
               novoAgendamento.getAnimal().getNome()
       );

    }


    public List<AgendamentoResponseDTO> agendamentos(Integer idTutor){

        List<Agendamento> agendamentos = agendamentoRepository.
                findByTutorIdOrderByDataAscHorarioAsc(idTutor);

        return agendamentos.stream()
                .map(agendamento -> new AgendamentoResponseDTO(
                        agendamento.getId(),
                        agendamento.getData(),
                        agendamento.getHorario(),
                        agendamento.getTipoServico(),
                        agendamento.getAnimal().getNome()
                ))
                .toList();
    }


    private Agendamento buscarEntidade(Integer id){

        return  agendamentoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Agendamento não encontrada com o id: "+id
                        ));
    }


    public AgendamentoResponseDTO update(Integer idAgendamento, AgendamentoRequestDTO update, Integer idTutor){

        var agendamentoExistente = agendamentoRepository.findByIdAndTutorId(idAgendamento, idTutor)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Agendamento não existente ou não pertence a esse tutor"
                ));

        var animal = animalRepository.findByIdAndTutorId(update.idAnimal(), idTutor)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Animal não encontro ou não pertence a esse tutor"
                        ));


        agendamentoExistente.setData(update.data());
        agendamentoExistente.setHorario(update.horario());
        agendamentoExistente.setTipoServico(update.servico());
        agendamentoExistente.setAnimal(animal);

       verificarConflito(agendamentoExistente);

        agendamentoRepository.save(agendamentoExistente);

        return new AgendamentoResponseDTO(
                agendamentoExistente.getId(),
                agendamentoExistente.getData(),
                agendamentoExistente.getHorario(),
                agendamentoExistente.getTipoServico(),
                agendamentoExistente.getAnimal().getNome()
        );

    }


    public void removerPorId(Integer idAgendamento, Integer idTutor){

        var agendamento = agendamentoRepository.findByIdAndTutorId(idAgendamento, idTutor)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Agendamento não encontrado ou não pertence a esse tutor"
                ));

        agendamentoRepository.delete(agendamento);

    }
}
