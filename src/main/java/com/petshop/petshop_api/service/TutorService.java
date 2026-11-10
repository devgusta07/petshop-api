package com.petshop.petshop_api.service;

import com.petshop.petshop_api.dto.response.animal.AnimalTutorResponseDTO;
import com.petshop.petshop_api.dto.response.tutor.RemocaoTutor;
import com.petshop.petshop_api.dto.request.tutor.TutorRequestDTO;
import com.petshop.petshop_api.dto.response.tutor.TutorResponseDTO;
import com.petshop.petshop_api.exception.RecursoEmUsoException;
import com.petshop.petshop_api.exception.RecursoJaExistenteException;
import com.petshop.petshop_api.exception.RecursoNaoEncontradoException;
import com.petshop.petshop_api.model.Animal;
import com.petshop.petshop_api.model.Tutor;
import com.petshop.petshop_api.repository.AnimalRepository;
import com.petshop.petshop_api.repository.TutorRepository;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class TutorService {

    private final TutorRepository tutorRepository;
    private final AnimalRepository animalRepository;

    public TutorService(TutorRepository tutorRepository,
                        AnimalRepository animalRepository) {
        this.tutorRepository = tutorRepository;
        this.animalRepository = animalRepository;
    }


    public TutorResponseDTO buscarTutorPorId(Integer id_tutor) {

        Tutor tutorEncontrado = tutorRepository.findById(id_tutor)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Tutor não encontrado com o id: " + id_tutor
                ));

        return new TutorResponseDTO(
                tutorEncontrado.getId(),
                tutorEncontrado.getNome(),
                tutorEncontrado.getContato(),
                tutorEncontrado.getEndereco()
        );
    }


    public TutorResponseDTO buscarPorContato(String cpf) {

        Tutor tutorEncontrado = tutorRepository.findByCpf(cpf)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Tutor não encontrado!"
                        ));

        return new TutorResponseDTO(
                tutorEncontrado.getId(),
                tutorEncontrado.getNome(),
                tutorEncontrado.getContato(),
                tutorEncontrado.getEndereco()
        );

    }



    private Tutor buscarEntidade(Integer id_tutor) {
        return  tutorRepository.findById(id_tutor)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                            "Tutor não encontrado com o id: "  + id_tutor
                ));
    }


    public RemocaoTutor removerPorId(Integer id_tuto) {

        TutorResponseDTO tutorExistente = buscarTutorPorId(id_tuto);

        if (animalRepository.existsByTutorId(id_tuto)) {

            throw new RecursoEmUsoException(
                    "Não é possível remover o tutor, pois existem animais vinculados a ele."
            );
        }

        tutorRepository.deleteById(tutorExistente.id());

        return new RemocaoTutor(
                "Tutor " + tutorExistente.nome() + ", removido com sucesso"
        );

    }


    public List<AnimalTutorResponseDTO> animaisVinculados(Integer id_Tutor){

        Tutor tutorExistente = buscarEntidade(id_Tutor);

        List<Animal> vinculados = tutorExistente.getAnimals();

       return vinculados.stream()
               .map(animal -> new AnimalTutorResponseDTO(
                       animal.getId(),
                       animal.getNome(),
                       animal.getRaca(),
                       animal.getPeso()
               ))
               .toList();

    }
}
