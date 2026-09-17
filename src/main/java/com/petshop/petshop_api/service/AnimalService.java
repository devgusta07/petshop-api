package com.petshop.petshop_api.service;

import com.petshop.petshop_api.dto.request.animal.AnimalRequestDTO;
import com.petshop.petshop_api.dto.response.animal.AnimalResponseDTO;
import com.petshop.petshop_api.dto.response.animal.RemocaoAnimal;
import com.petshop.petshop_api.dto.response.tutor.TutorResponseDTO;
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


    // Salvar animal

    public AnimalResponseDTO salvarAnimal(AnimalRequestDTO dto){

        Tutor tutor = tutorRepository.findById(dto.idTutor())
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
                animal.getId(),
                animal.getNome(),
                animal.getRaca(),
                animal.getPeso(),
                animal.getTutor().getId()
        );
    }


    //Buscar animal

    public AnimalResponseDTO buscarAnimalPorId(Integer id_Animal){

        Animal animalEncontrado = animalRepository.findById(id_Animal)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Nenhum animal encontrado com o id: " + id_Animal
                        ));

        return new AnimalResponseDTO(
                animalEncontrado.getId(),
                animalEncontrado.getNome(),
                animalEncontrado.getRaca(),
                animalEncontrado.getPeso(),
                animalEncontrado.getTutor().getId()
        );
    }


    //Buscar entidade

    private Animal entiadeAnimal(Integer id_animal){

        return animalRepository.findById(id_animal)
                .orElseThrow(() ->
                    new RecursoNaoEncontradoException(
                            "Nenhuma animal encontrado com o id: "+ id_animal
                    ));
    }


    //Remover animal

    public RemocaoAnimal removerAnimal(Integer id_animal){

        Animal animalExistente = entiadeAnimal(id_animal);

        animalRepository.delete(animalExistente);

         return new RemocaoAnimal(
                 "Animal removido com sucesso!\n" + animalExistente
         );
    }


    //Atualizar dados do animal

    public AnimalResponseDTO updateAnimal(Integer id_animal, AnimalRequestDTO updateAnimal){

        Animal animalExistente = entiadeAnimal(id_animal);

        animalExistente.setNome(updateAnimal.nome());
        animalExistente.setRaca(updateAnimal.raca());
        animalExistente.setPeso(updateAnimal.peso());

        animalRepository.save(animalExistente);

        return new AnimalResponseDTO(
                animalExistente.getId(),
                animalExistente.getNome(),
                animalExistente.getRaca(),
                animalExistente.getPeso(),
                animalExistente.getTutor().getId()
        );
    }


    //Buscar tutor vinculado ao  animal

    public TutorResponseDTO buscarTutorAnimal(Integer id_animal){

        Animal animalEncontrado = entiadeAnimal(id_animal);

        Tutor tutorVinculado = animalEncontrado.getTutor();

        return new TutorResponseDTO(
                tutorVinculado.getId(),
                tutorVinculado.getNome(),
                tutorVinculado.getContato(),
                tutorVinculado.getEndereco()
        );
    }


}
