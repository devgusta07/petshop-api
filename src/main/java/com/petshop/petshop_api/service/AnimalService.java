package com.petshop.petshop_api.service;

import com.petshop.petshop_api.dto.request.animal.AnimalRequestDTO;
import com.petshop.petshop_api.dto.response.animal.AnimalResponseDTO;
import com.petshop.petshop_api.exception.RecursoNaoEncontradoException;
import com.petshop.petshop_api.model.Animal;
import com.petshop.petshop_api.model.Tutor;
import com.petshop.petshop_api.repository.AnimalRepository;
import com.petshop.petshop_api.repository.TutorRepository;
import org.springframework.stereotype.Service;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final TutorRepository tutorRepository;

    public AnimalService(AnimalRepository animalRepository, TutorRepository tutorRepository) {
        this.animalRepository = animalRepository;
        this.tutorRepository = tutorRepository;
    }



    public AnimalResponseDTO salvarAnimal(AnimalRequestDTO dto, Integer id_Tutor) {

        Tutor tutor = tutorRepository.findById(id_Tutor)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Não há tutor cadastrado com esse id"
                        ));

        Animal animal = new Animal();
        animal.setNome(dto.nome());
        animal.setRaca(dto.raca());
        animal.setPeso(dto.peso());
        animal.setTutor(tutor);

        animalRepository.save(animal);

        return new AnimalResponseDTO(
                animal.getNome(),
                animal.getRaca(),
                animal.getPeso()
        );
    }


    public AnimalResponseDTO buscarAnimal(String nome, Integer id_Tutor) {

        Animal animalEncontrado = animalRepository.findByNomeAndTutorId(nome, id_Tutor)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Nenhum animal encontrado ou não pertence a este tutor!"
                        ));

        return new AnimalResponseDTO(
                animalEncontrado.getNome(),
                animalEncontrado.getRaca(),
                animalEncontrado.getPeso()
        );
    }


    public void removerAnimal(Integer id_animal,Integer id_tutor){

        Animal animalEncontrado = animalRepository.findByIdAndTutorId(id_animal, id_tutor)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Nenhum animal encontrado ou não pertence a este tutor!"
                        ));

        animalRepository.delete(animalEncontrado);

    }



    public AnimalResponseDTO updateAnimal(Integer id_animal,Integer id_tutor,
                                          AnimalRequestDTO updateAnimal){

        Animal animalExistente = animalRepository.findByIdAndTutorId(id_animal, id_tutor)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Nenhum animal encontrado ou não pertence a este tutor!"
                        ));

        animalExistente.setNome(updateAnimal.nome());
        animalExistente.setRaca(updateAnimal.raca());
        animalExistente.setPeso(updateAnimal.peso());

        animalRepository.save(animalExistente);

        return new AnimalResponseDTO(
                animalExistente.getNome(),
                animalExistente.getRaca(),
                animalExistente.getPeso()
        );
    }

}
