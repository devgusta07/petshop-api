package com.petshop.petshop_api.service;

import com.petshop.petshop_api.dto.response.animal.AnimalTutorResponseDTO;
import com.petshop.petshop_api.dto.response.tutor.TutorResponseDTO;
import com.petshop.petshop_api.exception.RecursoEmUsoException;
import com.petshop.petshop_api.exception.RecursoNaoEncontradoException;
import com.petshop.petshop_api.model.Animal;
import com.petshop.petshop_api.model.Tutor;
import com.petshop.petshop_api.repository.AgendamentoRepository;
import com.petshop.petshop_api.repository.AnimalRepository;
import com.petshop.petshop_api.repository.TutorRepository;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class TutorService {

    private final TutorRepository tutorRepository;
    private final AnimalRepository animalRepository;
    private final AgendamentoRepository agendamentoRepository;

    public TutorService(TutorRepository tutorRepository,
                        AnimalRepository animalRepository,
                        AgendamentoRepository agendamentoRepository) {
        this.tutorRepository = tutorRepository;
        this.animalRepository = animalRepository;
        this.agendamentoRepository = agendamentoRepository;
    }


    public TutorResponseDTO buscarTutorPorCpf(String cpf) {

        Tutor tutorEncontrado = tutorRepository.findByCpf(cpf)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Tutor não encontrado!, cpf incorreto! "
                ));

        return new TutorResponseDTO(
                tutorEncontrado.getNome(),
                tutorEncontrado.getContato(),
                tutorEncontrado.getEndereco(),
                tutorEncontrado.getCpf()
        );
    }


    public void removerPorCpf(String cpf) {

        Tutor tutorEncontrado = tutorRepository.findByCpf(cpf)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Tutor não encontrado!, cpf incorreto! "
                ));

        if (animalRepository.existsByTutorId(tutorEncontrado.getId())
                || agendamentoRepository.existsByTutorId(tutorEncontrado.getId())) {

            throw new RecursoEmUsoException(
                    "Não é possível remover o tutor, pois existem animais ou agendamentos vinculados a ele."
            );
        }

        tutorRepository.deleteByCpf(tutorEncontrado.getCpf());

    }


    public List<AnimalTutorResponseDTO> animaisVinculados(String cpf) {

        Tutor tutorEncontrado = tutorRepository.findByCpf(cpf)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Tutor não encontrado!, cpf incorreto! "
                ));

        List<Animal> vinculados = tutorEncontrado.getAnimals();

       return vinculados.stream()
               .map(animal -> new AnimalTutorResponseDTO(
                       animal.getNome(),
                       animal.getRaca(),
                       animal.getPeso()
               ))
               .toList();
    }
}
