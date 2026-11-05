package com.petshop.petshop_api.service;

import com.petshop.petshop_api.dto.request.loguin.LoguinRequest;
import com.petshop.petshop_api.exception.CredencialInvalidaException;
import com.petshop.petshop_api.exception.RecursoNaoEncontradoException;
import com.petshop.petshop_api.model.Tutor;
import com.petshop.petshop_api.repository.TutorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoguinServiceTest {

    @InjectMocks
    private LoguinService loguinService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private JwtEncoder jwtEncoder;

    @Test
    void deveRealizarloguinComSucesso() {

        LoguinRequest loguinRequest = new LoguinRequest("57046469812", "2809");
        Tutor tutor = new Tutor();
        tutor.setId(1);
        tutor.setNome("Gustavo");
        tutor.setContato("11945915617");
        tutor.setEndereco("rua n sei");
        tutor.setCpf("57046469812");
        tutor.setSenha("2809");

        when(tutorRepository.findByCpf(loguinRequest.cpf()))
                .thenReturn(Optional.of(tutor));

        when(passwordEncoder.matches(loguinRequest.senha(), tutor.getSenha()))
                .thenReturn(true);

        Jwt jwt = Jwt.withTokenValue("token-fake")
                .header("token", "token-fake")
                .claim("sub", tutor.getId().toString())
                .build();

        when(jwtEncoder.encode(any(JwtEncoderParameters.class)))
                .thenReturn(jwt);

        var loguinResponse = loguinService.loguin(loguinRequest);
        assertNotNull(loguinResponse);
        assertEquals("token-fake", loguinResponse.jwt());

        verify(jwtEncoder).encode(any(JwtEncoderParameters.class));
    }

    @Test
    void deveLancarExcecaoTutorNaoEncontrado() {

        LoguinRequest loguinRequest = new LoguinRequest("57046469812", "2809");
        Tutor tutor = new Tutor();

        when(tutorRepository.findByCpf(loguinRequest.cpf()))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
                () -> loguinService.loguin(loguinRequest));

        assertEquals("Tutor não encontrado!", exception.getMessage());

    }

    @Test
    void deveLancarExcecaoSenhaIncorreta() {

        LoguinRequest loguinRequest = new LoguinRequest("57046469812", "2809");
        Tutor tutor = new Tutor();
        tutor.setSenha("senha-criptografada");

        when(tutorRepository.findByCpf(loguinRequest.cpf()))
                .thenReturn(Optional.of(tutor));

        when(passwordEncoder.matches(loguinRequest.senha(), tutor.getSenha()))
                .thenReturn(false);

        CredencialInvalidaException exception = assertThrows(CredencialInvalidaException.class,
                () -> loguinService.loguin(loguinRequest));

        assertEquals("Cpf ou senha invalido!", exception.getMessage());

    }
}