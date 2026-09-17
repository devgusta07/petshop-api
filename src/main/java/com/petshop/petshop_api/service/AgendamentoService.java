package com.petshop.petshop_api.service;

import com.petshop.petshop_api.dto.request.agendamento.AgendamentoRequestDTO;
import com.petshop.petshop_api.dto.response.agendamento.AgendamentoResponseDTO;
import com.petshop.petshop_api.dto.response.agendamento.RemocaoAgendamento;
import com.petshop.petshop_api.exception.RecursoJaExistenteException;
import com.petshop.petshop_api.exception.RecursoNaoEncontradoException;
import com.petshop.petshop_api.model.Agendamento;
import com.petshop.petshop_api.model.Animal;
import com.petshop.petshop_api.repository.AgendamentoRepository;
import com.petshop.petshop_api.repository.AnimalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final AnimalRepository animalRepository;

    public AgendamentoService(AgendamentoRepository agendamentoRespository, AnimalRepository animalRepository) {
        this.agendamentoRepository = agendamentoRespository;
        this.animalRepository = animalRepository;
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


    public AgendamentoResponseDTO realizarAgendamento(AgendamentoRequestDTO agendamento){

        Animal animal = animalRepository.findById(agendamento.idAnimal())
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Animal nao encontra com o id: "+agendamento.idAnimal()
                        ));

        Agendamento novoAgendamento = new Agendamento();
        novoAgendamento.setData(agendamento.data());
        novoAgendamento.setHorario(agendamento.horario());
        novoAgendamento.setTipoServico(agendamento.servico());
        novoAgendamento.setAnimal(animal);

       verificarConflito(novoAgendamento);

       agendamentoRepository.save(novoAgendamento);

       return new AgendamentoResponseDTO(
               novoAgendamento.getId(),
               novoAgendamento.getData(),
               novoAgendamento.getHorario(),
               novoAgendamento.getTipoServico(),
               novoAgendamento.getAnimal().getId()
       );

    }


    public AgendamentoResponseDTO buscarPorId(Integer id){

        Agendamento agendamentoExistente = agendamentoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "id " + id + " não encontrado!"
                        ));

        return new AgendamentoResponseDTO(
                agendamentoExistente.getId(),
                agendamentoExistente.getData(),
                agendamentoExistente.getHorario(),
                agendamentoExistente.getTipoServico(),
                agendamentoExistente.getAnimal().getId()
        );

    }


    private Agendamento buscarEntidade(Integer id){

        return  agendamentoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Agendamento não encontrada com o id: "+id
                        ));
    }


    public AgendamentoResponseDTO update(Integer id, AgendamentoRequestDTO update){

        Agendamento agendamentoExistente = buscarEntidade(id);

        Animal animal = animalRepository.findById(update.idAnimal())
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Animal nao encontra com o id: "+ update.idAnimal()
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
                agendamentoExistente.getAnimal().getId()
        );

    }



    public RemocaoAgendamento removerPorId(Integer id){

        Agendamento agendamentoExistente = buscarEntidade(id);

        agendamentoRepository.delete(agendamentoExistente);

        return new RemocaoAgendamento(
                "Agendamento : " + agendamentoExistente.getId() + ", Removido com sucesso!!"
        );
    }
}
