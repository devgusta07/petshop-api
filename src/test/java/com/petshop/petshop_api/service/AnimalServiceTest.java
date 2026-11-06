package com.petshop.petshop_api.service;

import com.petshop.petshop_api.dto.request.animal.AnimalRequestDTO;
import com.petshop.petshop_api.dto.response.animal.AnimalResponseDTO;
import com.petshop.petshop_api.exception.RecursoNaoEncontradoException;
import com.petshop.petshop_api.model.Animal;
import com.petshop.petshop_api.model.Tutor;
import com.petshop.petshop_api.repository.AnimalRepository;
import com.petshop.petshop_api.repository.TutorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {

    @InjectMocks
    private AnimalService animalService;

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Test
    void deveSalvarAnimal() {

        Integer id_Tutor = 22;

        AnimalRequestDTO dto = new AnimalRequestDTO("Cesar", "vira-lata", 28.89);

        Tutor tutor = new Tutor("Gustavo", "11945985617", "rua" );


        when(tutorRepository.findById(id_Tutor))
                .thenReturn(Optional.of(tutor));

        AnimalResponseDTO animal = animalService.salvarAnimal(dto, id_Tutor);
        assertNotNull(animal);

        verify(tutorRepository).findById(id_Tutor);
        verify(animalRepository).save(any(Animal.class));
    }

    @Test
    void deveLancarExcecaoTutorNaoCadastrado() {

        Integer id_Tutor = 22;

        AnimalRequestDTO dto = new AnimalRequestDTO("Cesar", "vira-lata", 28.89);

        when(tutorRepository.findById(id_Tutor))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException excecao = assertThrows(RecursoNaoEncontradoException.class,
                () -> animalService.salvarAnimal(dto, id_Tutor));

        assertEquals("Não há tutor cadastrado com esse id", excecao.getMessage());
        verify(animalRepository, never()).save(any(Animal.class));

    }

    @Test
    void deveBuscarAnimal() {

        String nome = "rex";
        Integer id_Tutor = 22;

        var tutor = new Tutor();
        tutor.setId(id_Tutor);

        Animal animal = new Animal();
        animal.setNome(nome);
        animal.setTutor(tutor);

        when(animalRepository.findByNomeAndTutorId(nome, id_Tutor))
            .thenReturn(Optional.of(animal));

        AnimalResponseDTO animalResponseDTO = animalService.buscarAnimal(nome, id_Tutor);
        assertNotNull(animalResponseDTO);

        verify(animalRepository).findByNomeAndTutorId(nome, id_Tutor);
    }

    @Test
    void deveLancarExcecaoAnimalNaoCadastradoOuNaoPertencente(){

        String nome = "rex";
        Integer id_Tutor = 22;

        when(animalRepository.findByNomeAndTutorId(nome, id_Tutor))
            .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
                () -> animalService.buscarAnimal(nome, id_Tutor));

        assertEquals("Nenhum animal encontrado ou não pertence a este tutor!", exception.getMessage());
        verify(animalRepository).findByNomeAndTutorId(nome, id_Tutor);

    }


    @Test
    void deveRemoverAnimal() {

        Integer id_Tutor = 22;
        Integer id_Animal = 12;

        var animal = new Animal();

        when(animalRepository.findByIdAndTutorId(id_Animal, id_Tutor))
                .thenReturn(Optional.of(animal));

        animalService.removerAnimal(id_Animal, id_Tutor);

        verify(animalRepository).delete(animal);
        verify(animalRepository).findByIdAndTutorId(id_Animal, id_Tutor);
    }

    @Test
    void deveLancarExcecaoAnimalNaoEncontradoOuNaoPertencente(){

        Integer id_Tutor = 22;
        Integer id_Animal = 12;


        when(animalRepository.findByIdAndTutorId(id_Animal, id_Tutor))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
                () -> animalService.removerAnimal(id_Animal, id_Tutor));

        assertEquals("Nenhum animal encontrado ou não pertence a este tutor!", exception.getMessage());
        verify(animalRepository).findByIdAndTutorId(id_Animal, id_Tutor);

    }

    @Test
    void deveFazerUpdateAnimal() {

        Integer id_Tutor = 22;
        Integer id_Animal = 12;

        AnimalRequestDTO dto = new AnimalRequestDTO("Cesar", "vira-lata", 28.89);

        var animal = new Animal();

        when(animalRepository.findByIdAndTutorId(id_Animal, id_Tutor))
                .thenReturn(Optional.of(animal));

        AnimalResponseDTO resposta = animalService.updateAnimal(id_Animal, id_Tutor, dto);

        assertNotNull(resposta);
        assertEquals("Cesar", resposta.nome());
        assertEquals("vira-lata", resposta.raca());
        assertEquals(28.89, resposta.peso());

        verify(animalRepository).findByIdAndTutorId(id_Animal, id_Tutor);
        verify(animalRepository).save(any(Animal.class));

    }
}