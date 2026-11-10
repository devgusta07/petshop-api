package com.petshop.petshop_api.service;

import static org.junit.jupiter.api.Assertions.*;

import com.petshop.petshop_api.dto.request.agendamento.AgendamentoRequestDTO;
import com.petshop.petshop_api.dto.response.agendamento.AgendamentoResponseDTO;
import com.petshop.petshop_api.exception.RecursoJaExistenteException;
import com.petshop.petshop_api.exception.RecursoNaoEncontradoException;
import com.petshop.petshop_api.model.Agendamento;
import com.petshop.petshop_api.model.Animal;
import com.petshop.petshop_api.model.Tutor;
import com.petshop.petshop_api.model.TipoServico;
import com.petshop.petshop_api.repository.AgendamentoRepository;
import com.petshop.petshop_api.repository.AnimalRepository;
import com.petshop.petshop_api.repository.TutorRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendamentoServiceTest {

    @ExtendWith(MockitoExtension.class)

        @Mock
        private AgendamentoRepository agendamentoRepository;

        @Mock
        private AnimalRepository animalRepository;

        @Mock
        private TutorRepository tutorRepository;

        @InjectMocks
        private AgendamentoService agendamentoService;

        private Tutor tutor;
        private Animal animal;
        private Agendamento agendamento;
        private AgendamentoRequestDTO request;

        private final Integer ID_TUTOR = 1;
        private final Integer ID_ANIMAL = 10;
        private final Integer ID_AGENDAMENTO = 100;

        private final TipoServico SERVICO = TipoServico.values()[0];

        @BeforeEach
        void setUp() {

            tutor = new Tutor();
            tutor.setId(ID_TUTOR);

            animal = new Animal();
            animal.setId(ID_ANIMAL);
            animal.setNome("Rex");

            agendamento = new Agendamento();
            agendamento.setId(ID_AGENDAMENTO);
            agendamento.setData(LocalDate.of(2026, 11, 15));
            agendamento.setHorario(LocalTime.of(14, 0));
            agendamento.setTipoServico(SERVICO);
            agendamento.setAnimal(animal);
            agendamento.setTutor(tutor);

            request = new AgendamentoRequestDTO(
                    LocalDate.of(2026, 11, 15),
                    LocalTime.of(14, 0),
                    SERVICO,
                    ID_ANIMAL
            );
        }

    @Test
    void deveRealizarAgendamentoComSucesso() {

        when(animalRepository.findByIdAndTutorId(ID_ANIMAL, ID_TUTOR))
                .thenReturn(Optional.of(animal));

        when(tutorRepository.findById(ID_TUTOR))
                .thenReturn(Optional.of(tutor));

        when(agendamentoRepository.findByData(request.data()))
                .thenReturn(List.of());

        when(agendamentoRepository.save(any(Agendamento.class)))
                .thenAnswer(invocation -> {
                    Agendamento salvo = invocation.getArgument(0);
                    salvo.setId(ID_AGENDAMENTO);
                    return salvo;
                });

        AgendamentoResponseDTO response =
                agendamentoService.realizarAgendamento(request, ID_TUTOR);

        assertNotNull(response);
        assertEquals(ID_AGENDAMENTO, response.id());
        assertEquals("Rex", response.nome());

        verify(agendamentoRepository).save(any(Agendamento.class));
    }

        @Test
        void deveLancarExcecaoQuandoAnimalNaoPertenceAoTutor() {

            when(animalRepository.findByIdAndTutorId(ID_ANIMAL, ID_TUTOR))
                    .thenReturn(Optional.empty());

            assertThrows(RecursoNaoEncontradoException.class,
                    () -> agendamentoService.realizarAgendamento(request, ID_TUTOR));

            verify(agendamentoRepository, never()).save(any());
        }

        @Test
        void deveLancarExcecaoQuandoTutorNaoExiste() {

            when(animalRepository.findByIdAndTutorId(ID_ANIMAL, ID_TUTOR))
                    .thenReturn(Optional.of(animal));

            when(tutorRepository.findById(ID_TUTOR))
                    .thenReturn(Optional.empty());

            assertThrows(RecursoNaoEncontradoException.class,
                    () -> agendamentoService.realizarAgendamento(request, ID_TUTOR));

            verify(agendamentoRepository, never()).save(any());
        }

        @Test
        void deveLancarExcecaoQuandoExisteConflitoDeHorario() {

            Agendamento existente = new Agendamento();
            existente.setId(200);
            existente.setHorario(LocalTime.of(14, 30));

            when(animalRepository.findByIdAndTutorId(ID_ANIMAL, ID_TUTOR))
                    .thenReturn(Optional.of(animal));

            when(tutorRepository.findById(ID_TUTOR))
                    .thenReturn(Optional.of(tutor));

            when(agendamentoRepository.findByData(request.data()))
                    .thenReturn(List.of(existente));

            assertThrows(RecursoJaExistenteException.class,
                    () -> agendamentoService.realizarAgendamento(request, ID_TUTOR));

            verify(agendamentoRepository, never()).save(any());
        }

        @Test
        void deveListarAgendamentosDoTutor() {

            when(agendamentoRepository.findByTutorIdOrderByDataAscHorarioAsc(ID_TUTOR))
                    .thenReturn(List.of(agendamento));

            List<AgendamentoResponseDTO> response =
                    agendamentoService.agendamentos(ID_TUTOR);

            assertEquals(1, response.size());
            assertEquals("Rex", response.get(0).nome());
            assertEquals(ID_AGENDAMENTO, response.get(0).id());
        }

        @Test
        void deveRetornarListaVaziaQuandoTutorNaoPossuiAgendamentos() {

            when(agendamentoRepository.findByTutorIdOrderByDataAscHorarioAsc(ID_TUTOR))
                    .thenReturn(List.of());

            List<AgendamentoResponseDTO> response =
                    agendamentoService.agendamentos(ID_TUTOR);

            assertTrue(response.isEmpty());
        }

        @Test
        void deveAtualizarAgendamentoComSucesso() {

            AgendamentoRequestDTO update = new AgendamentoRequestDTO(
                    LocalDate.of(2026, 11, 16),
                    LocalTime.of(15, 0),
                    SERVICO,
                    ID_ANIMAL
            );

            when(agendamentoRepository.findByIdAndTutorId(ID_AGENDAMENTO, ID_TUTOR))
                    .thenReturn(Optional.of(agendamento));

            when(animalRepository.findByIdAndTutorId(ID_ANIMAL, ID_TUTOR))
                    .thenReturn(Optional.of(animal));

            when(agendamentoRepository.findByData(update.data()))
                    .thenReturn(List.of(agendamento));

            when(agendamentoRepository.save(any(Agendamento.class)))
                    .thenReturn(agendamento);

            AgendamentoResponseDTO response =
                    agendamentoService.update(ID_AGENDAMENTO, update, ID_TUTOR);

            assertNotNull(response);
            assertEquals(update.data(), response.data());
            assertEquals(update.horario(), response.horario());
            assertEquals("Rex", response.nome());

            verify(agendamentoRepository).save(agendamento);
        }

        @Test
        void deveLancarExcecaoQuandoAgendamentoNaoExisteNoUpdate() {

            when(agendamentoRepository.findByIdAndTutorId(ID_AGENDAMENTO, ID_TUTOR))
                    .thenReturn(Optional.empty());

            assertThrows(RecursoNaoEncontradoException.class,
                    () -> agendamentoService.update(ID_AGENDAMENTO, request, ID_TUTOR));

            verify(agendamentoRepository, never()).save(any());
        }

        @Test
        void deveLancarExcecaoQuandoAnimalNaoPertenceAoTutorNoUpdate() {

            when(agendamentoRepository.findByIdAndTutorId(ID_AGENDAMENTO, ID_TUTOR))
                    .thenReturn(Optional.of(agendamento));

            when(animalRepository.findByIdAndTutorId(ID_ANIMAL, ID_TUTOR))
                    .thenReturn(Optional.empty());

            assertThrows(RecursoNaoEncontradoException.class,
                    () -> agendamentoService.update(ID_AGENDAMENTO, request, ID_TUTOR));

            verify(agendamentoRepository, never()).save(any());
        }

        @Test
        void deveLancarExcecaoQuandoUpdateGeraConflitoDeHorario() {

            Agendamento existente = new Agendamento();
            existente.setId(200);
            existente.setHorario(LocalTime.of(14, 30));

            when(agendamentoRepository.findByIdAndTutorId(ID_AGENDAMENTO, ID_TUTOR))
                    .thenReturn(Optional.of(agendamento));

            when(animalRepository.findByIdAndTutorId(ID_ANIMAL, ID_TUTOR))
                    .thenReturn(Optional.of(animal));

            when(agendamentoRepository.findByData(request.data()))
                    .thenReturn(List.of(agendamento, existente));

            assertThrows(RecursoJaExistenteException.class,
                    () -> agendamentoService.update(ID_AGENDAMENTO, request, ID_TUTOR));

            verify(agendamentoRepository, never()).save(any());
        }

        @Test
        void deveRemoverAgendamentoComSucesso() {

            when(agendamentoRepository.findByIdAndTutorId(ID_AGENDAMENTO, ID_TUTOR))
                    .thenReturn(Optional.of(agendamento));

            assertDoesNotThrow(() ->
                    agendamentoService.removerPorId(ID_AGENDAMENTO, ID_TUTOR));

            verify(agendamentoRepository).delete(agendamento);
        }

        @Test
        void deveLancarExcecaoQuandoAgendamentoNaoExisteNaRemocao() {

            when(agendamentoRepository.findByIdAndTutorId(ID_AGENDAMENTO, ID_TUTOR))
                    .thenReturn(Optional.empty());

            assertThrows(RecursoNaoEncontradoException.class,
                    () -> agendamentoService.removerPorId(ID_AGENDAMENTO, ID_TUTOR));

            verify(agendamentoRepository, never()).delete(any());
        }
    }


