package com.petshop.petshop_api.service;

import com.petshop.petshop_api.dto.request.tutor.TutorRequestDTO;
import com.petshop.petshop_api.dto.response.tutor.TutorResponseDTO;
import com.petshop.petshop_api.exception.RecursoJaExistenteException;
import com.petshop.petshop_api.exception.RecursoNaoEncontradoException;
import com.petshop.petshop_api.model.RoleName;
import com.petshop.petshop_api.model.Roles;
import com.petshop.petshop_api.model.Tutor;
import com.petshop.petshop_api.repository.RolesRepository;
import com.petshop.petshop_api.repository.TutorRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreatService {

    private final PasswordEncoder passwordEncoder;
    private final TutorRepository tutorRepository;
    private final RolesRepository rolesRepository;

    public CreatService(PasswordEncoder passwordEncoder, TutorRepository tutorRepository, RolesRepository rolesRepository) {
        this.passwordEncoder = passwordEncoder;
        this.tutorRepository = tutorRepository;
        this.rolesRepository = rolesRepository;
    }

    public TutorResponseDTO createTutor(TutorRequestDTO request) {

        if(tutorRepository.existsByCpf(request.cpf())){

            throw new RecursoJaExistenteException("Tutor existente!");
        }

        Tutor tutor = new Tutor();
        tutor.setNome(request.nome());
        tutor.setContato(request.contato());
        tutor.setEndereco(request.endereco());
        tutor.setCpf(request.cpf());
        tutor.setSenha(passwordEncoder.encode(request.senha()));

        Roles user = rolesRepository.findByName(RoleName.USER)
                        .orElseThrow(() -> new RecursoNaoEncontradoException(
                                "Role não encontrada"
                        ));
        tutor.getRoles().add(user);

        tutorRepository.save(tutor);

        return new TutorResponseDTO(
                tutor.getId(),
                tutor.getNome(),
                tutor.getContato(),
                tutor.getEndereco()
        );

    }
}
