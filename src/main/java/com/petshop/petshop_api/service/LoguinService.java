package com.petshop.petshop_api.service;

import com.petshop.petshop_api.dto.request.loguin.LoguinRequest;
import com.petshop.petshop_api.dto.response.loguin.LoguinResponse;
import com.petshop.petshop_api.exception.CredencialInvalidaException;
import com.petshop.petshop_api.exception.RecursoNaoEncontradoException;
import com.petshop.petshop_api.model.Tutor;
import com.petshop.petshop_api.repository.TutorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class LoguinService {

    private final PasswordEncoder passwordEncoder;
    private final TutorRepository tutorRepository;
    private final JwtEncoder jwtEncoder;

    public LoguinService(PasswordEncoder passwordEncoder, TutorRepository tutorRepository, JwtEncoder jwtEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.tutorRepository = tutorRepository;
        this.jwtEncoder = jwtEncoder;
    }

    public LoguinResponse loguin(LoguinRequest loguinRequest) {

        Tutor tutor = tutorRepository.findByCpf(loguinRequest.cpf())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Tutor não encontrado!"
                ));

        if(!passwordEncoder.matches(loguinRequest.senha(), tutor.getSenha())) {

            throw new CredencialInvalidaException("Cpf ou senha invalido!");
        }

        var roles = tutor.getRoles()
                .stream()
                .map(role -> role.getName().name())
                .toList();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(tutor.getId().toString())
                .claim("roles", roles)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(300))
                .build();

        JwtEncoderParameters parameters = JwtEncoderParameters.from(claims);

        String token = jwtEncoder.encode(parameters).getTokenValue();

        return new LoguinResponse(token);

    }
}
