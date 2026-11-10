package com.petshop.petshop_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tutor")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "animals")
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tutor")
    private Integer id;

    @NotBlank(message = "Nome é obrigatorio")
    @Column(nullable = false)
    private String nome;


    @Column(nullable = false)
    @NotBlank(message = "Contato é obrigatório")
    @Pattern(
            regexp = "^\\d{10,11}$",
            message = "Telefone inválido! Informe DDD + número sem espaços ou traços."
    )
    private String contato;


    @Column(nullable = false)
    @NotBlank(message = "Endereço e obrigatorio")
    private String endereco;

    @NotBlank
    private String cpf;

    @NotBlank
    private String senha;

    @OneToMany(mappedBy = "tutor")
    private List<Animal> animals = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "roles_tutores",
            joinColumns = @JoinColumn(name = "tutor_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Roles> roles = new HashSet<>();

    @OneToMany(mappedBy = "tutor")
    private List<Agendamento> agendamento;

    public Tutor(String nome, String contato, String endereco) {
        this.nome = nome;
        this.contato = contato;
        this.endereco = endereco;
    }


}
