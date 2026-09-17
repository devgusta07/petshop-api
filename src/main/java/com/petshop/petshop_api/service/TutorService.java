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


    //salvar tutor

    public TutorResponseDTO salvarTutor(TutorRequestDTO dto) {

        if (tutorRepository.existsByContato(dto.contato())) {

            throw new RecursoJaExistenteException(
                    "Já existe um tutor cadastrado com este contato!"
            );
        }

        Tutor tutor = new Tutor();
        tutor.setNome(dto.nome());
        tutor.setContato(dto.contato());
        tutor.setEndereco(dto.endereco());

        tutorRepository.save(tutor);

        return new TutorResponseDTO(
                tutor.getId(),
                tutor.getNome(),
                tutor.getContato(),
                tutor.getEndereco()
        );
    }


    //Buscar tutor por id

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


    //Buscar tutor por contato

    public TutorResponseDTO buscarPorContato(String contato) {

        Tutor tutorEncontrado = tutorRepository.findByContato(contato)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Tutor não encontrado com o número: " + contato
                        ));

        return new TutorResponseDTO(
                tutorEncontrado.getId(),
                tutorEncontrado.getNome(),
                tutorEncontrado.getContato(),
                tutorEncontrado.getEndereco()
        );

    }


    //Buscar entidade tutor

    private Tutor buscarEntidade(Integer id_tutor) {
        return  tutorRepository.findById(id_tutor)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                            "Tutor não encontrado com o id: "  + id_tutor
                ));
    }


    //Remover tutor

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


    // Atualizar dados do Tutor

    public TutorResponseDTO updateTutor(Integer id_tutor, TutorRequestDTO updateTutor){

        Tutor tutorEntidade = buscarEntidade(id_tutor);

        if(!tutorEntidade.getContato().equals(updateTutor.contato())
            && tutorRepository.existsByContato(updateTutor.contato())) {

            throw new RecursoJaExistenteException(
                    "Já existe outro tutor cadastrado com esse contato!"
            );
        }

        tutorEntidade.setNome(updateTutor.nome());
        tutorEntidade.setContato(updateTutor.contato());
        tutorEntidade.setEndereco(updateTutor.endereco());

         tutorRepository.save(tutorEntidade);

         return new TutorResponseDTO(
                 tutorEntidade.getId(),
                 tutorEntidade.getNome(),
                 tutorEntidade.getContato(),
                 tutorEntidade.getEndereco()
         );

    }


    //Animais vinculados ao Tutor

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
