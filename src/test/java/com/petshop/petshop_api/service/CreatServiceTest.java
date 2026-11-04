package com.petshop.petshop_api.service;

import com.petshop.petshop_api.dto.request.tutor.TutorRequestDTO;
import com.petshop.petshop_api.dto.response.tutor.TutorResponseDTO;
import com.petshop.petshop_api.exception.RecursoJaExistenteException;
import com.petshop.petshop_api.exception.RecursoNaoEncontradoException;
import com.petshop.petshop_api.model.RoleName;
import com.petshop.petshop_api.model.Roles;
import com.petshop.petshop_api.model.Tutor;
import com.petshop.petshop_api.repository.AnimalRepository;
import com.petshop.petshop_api.repository.RolesRepository;
import com.petshop.petshop_api.repository.TutorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreatServiceTest {

    @InjectMocks
    private CreatService creatService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private RolesRepository rolesRepository;

    @Test
    void deveCriarTutor() {

        TutorRequestDTO resquest = new TutorRequestDTO("Gustavo", "11945915618",
                "Rua do calmo", "570464", "2809");

        Roles role = new Roles();
        role.setName(RoleName.USER);

        when(tutorRepository.existsByCpf(resquest.cpf()))
                .thenReturn(false);

        when(passwordEncoder.encode(resquest.senha()))
                .thenReturn(resquest.senha());

        when(rolesRepository.findByName(RoleName.USER))
                .thenReturn(Optional.of(role));

        TutorResponseDTO response = creatService.createTutor(resquest);
        assertNotNull(response);

        verify(tutorRepository).save(any(Tutor.class));
    }

    @Test
    void deveLancarExcecaoQuandoCpfJaExiste() {

        TutorRequestDTO resquest = new TutorRequestDTO("Gustavo", "11945915618",
                "Rua do calmo", "570464", "2809");


        when(tutorRepository.existsByCpf(resquest.cpf()))
                .thenReturn(true);

        RecursoJaExistenteException exception = assertThrows(RecursoJaExistenteException.class,
                () -> creatService.createTutor(resquest));

        assertEquals("Tutor existente!", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoRoleNaoEncontrado() {

        TutorRequestDTO resquest = new TutorRequestDTO("Gustavo", "11945915618",
                "Rua do calmo", "570464", "2809");


        when(tutorRepository.existsByCpf(resquest.cpf()))
                .thenReturn(false);

        when(passwordEncoder.encode(resquest.senha()))
                .thenReturn(resquest.senha());

        when(rolesRepository.findByName(RoleName.USER))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
                () -> creatService.createTutor(resquest));

        assertEquals("Role não encontrada", exception.getMessage());

    }
}