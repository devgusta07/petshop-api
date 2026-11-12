package com.petshop.petshop_api.service;

import com.petshop.petshop_api.dto.request.tutor.TutorRequestDTO;
import com.petshop.petshop_api.dto.response.animal.AnimalTutorResponseDTO;
import com.petshop.petshop_api.dto.response.tutor.TutorResponseDTO;
import com.petshop.petshop_api.exception.RecursoEmUsoException;
import com.petshop.petshop_api.exception.RecursoNaoEncontradoException;
import com.petshop.petshop_api.model.Animal;
import com.petshop.petshop_api.model.Tutor;
import com.petshop.petshop_api.repository.AgendamentoRepository;
import com.petshop.petshop_api.repository.AnimalRepository;
import com.petshop.petshop_api.repository.TutorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TutorServiceTest {

    @InjectMocks
    private TutorService tutorService;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private AgendamentoRepository agendamentoRepository;

    private  Tutor tutor;
    private TutorRequestDTO request;
    private Animal animal;

    private final String CPF_TUTOR = "1234567890";
    private final Integer ID_TUTOR = 12345;
    private final Integer ID_ANIMAL = 12345;


    @BeforeEach
    void setUp() {



        animal = new Animal();
        animal.setId(ID_ANIMAL);
        animal.setNome("rex");
        animal.setRaca("pit");
        animal.setPeso(12.23);


        tutor = new Tutor();
        tutor.setId(ID_TUTOR);
        tutor.setCpf(CPF_TUTOR);
        tutor.setAnimals(List.of(animal));

        animal.setTutor(tutor);

        request = new TutorRequestDTO(
                tutor.getNome(),
                tutor.getContato(),
                tutor.getEndereco(),
                tutor.getCpf(),
                tutor.getSenha()
        );
    }

    @Test
    void DevebuscarTutorPorCpf() {

        when(tutorRepository.findByCpf(CPF_TUTOR))
                .thenReturn(Optional.of(tutor));

        TutorResponseDTO resoponse =
                tutorService.buscarTutorPorCpf(request.cpf());

        assertNotNull(resoponse);
        assertEquals(CPF_TUTOR, resoponse.cpf());

        verify(tutorRepository).findByCpf(CPF_TUTOR);

    }

    @Test
    void DeveLancarExcecaoRecursoNaoEncontrado1(){

        when(tutorRepository.findByCpf(CPF_TUTOR))
            .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception =
                assertThrows(RecursoNaoEncontradoException.class,
                        () -> tutorService.buscarTutorPorCpf(request.cpf()));

        assertEquals("Tutor não encontrado!, cpf incorreto! ", exception.getMessage());
        verify(tutorRepository).findByCpf(CPF_TUTOR);
    }


    @Test
    void DeveremoverTutorPorCpf() {

        when(tutorRepository.findByCpf(CPF_TUTOR))
                .thenReturn(Optional.of(tutor));

        when(animalRepository.existsByTutorId(ID_TUTOR))
            .thenReturn(false);

        when(agendamentoRepository.existsByTutorId(ID_TUTOR))
            .thenReturn(false);

        tutorService.removerPorCpf(request.cpf());

        verify(tutorRepository).findByCpf(CPF_TUTOR);
        verify(animalRepository).existsByTutorId(ID_TUTOR);
        verify(agendamentoRepository).existsByTutorId(ID_TUTOR);

        verify(tutorRepository).deleteByCpf(CPF_TUTOR);
    }

    @Test
    void DeveLancarExcecaoRecursoNaoEncontrado2(){

        when(tutorRepository.findByCpf(CPF_TUTOR))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception =
                assertThrows(RecursoNaoEncontradoException.class,
                        () -> tutorService.removerPorCpf(request.cpf()));

        assertEquals("Tutor não encontrado!, cpf incorreto! ", exception.getMessage());
        verify(tutorRepository).findByCpf(CPF_TUTOR);
    }

    @Test
    void DeverLancarExececaoQuandoTiverAnimaisVinculados() {

        when(tutorRepository.findByCpf(CPF_TUTOR))
            .thenReturn(Optional.of(tutor));


        when(animalRepository.existsByTutorId(ID_TUTOR))
            .thenReturn(true);

        RecursoEmUsoException exception =
                assertThrows(RecursoEmUsoException.class,
                        () -> tutorService.removerPorCpf(request.cpf()));

        assertEquals("Não é possível remover o tutor, pois existem animais ou agendamentos vinculados a ele.",
                exception.getMessage());

        verify(tutorRepository).findByCpf(CPF_TUTOR);
        verify(animalRepository).existsByTutorId(ID_TUTOR);
    }

    @Test
    void DeverLancarExececaoQuandoTiverAgendamentosVinculados() {

        when(tutorRepository.findByCpf(CPF_TUTOR))
                .thenReturn(Optional.of(tutor));

        when(animalRepository.existsByTutorId(ID_TUTOR))
                .thenReturn(false);

        when(agendamentoRepository.existsByTutorId(ID_TUTOR))
                .thenReturn(true);


        RecursoEmUsoException exception =
                assertThrows(RecursoEmUsoException.class,
                        () -> tutorService.removerPorCpf(request.cpf()));

        assertEquals("Não é possível remover o tutor, pois existem animais ou agendamentos vinculados a ele.",
                exception.getMessage());

        verify(tutorRepository).findByCpf(CPF_TUTOR);
        verify(animalRepository).existsByTutorId(ID_TUTOR);
        verify(agendamentoRepository).existsByTutorId(ID_TUTOR);
    }


    @Test
    void DeveTrazerAnimaisVinculados() {

        when(tutorRepository.findByCpf(CPF_TUTOR))
            .thenReturn(Optional.of(tutor));

        List<AnimalTutorResponseDTO> response =
                tutorService.animaisVinculados(tutor.getCpf());

        assertNotNull(response);
        assertEquals(1, response.size());

        assertEquals("rex", response.get(0).nome());
        assertEquals("pit", response.get(0).raca());
        assertEquals(12.23, response.get(0).peso());

        verify(tutorRepository).findByCpf(CPF_TUTOR);
    }

    @Test
    void  DeveLancarExcecaoRecursoNaoEncontrado3(){

        when(tutorRepository.findByCpf(CPF_TUTOR))
            .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
                () -> tutorService.animaisVinculados(CPF_TUTOR));

        assertEquals("Tutor não encontrado!, cpf incorreto! ", exception.getMessage());
        verify(tutorRepository).findByCpf(CPF_TUTOR);

    }
}