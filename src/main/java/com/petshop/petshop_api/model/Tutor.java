package com.petshop.petshop_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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


    @OneToMany(mappedBy = "tutor")
    private List<Animal> animals = new ArrayList<>();

    public Tutor(String nome, String contato, String endereco) {
        this.nome = nome;
        this.contato = contato;
        this.endereco = endereco;
    }


}
