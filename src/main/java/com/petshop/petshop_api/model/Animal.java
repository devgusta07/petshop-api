package com.petshop.petshop_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "animal")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"tutor", "agendamentos"})
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_animal", nullable = false)
    private Integer id;

    @Column(nullable = false)
    @NotBlank(message = "Obrigatorio informar o nome!")
    private String nome;

    @Column(nullable = false)
    @NotBlank(message = "Obrigatorio informar a raça!")
    private String raca;


    @Column(nullable = false)
    @Positive(message = "O peso dev ser maior que zero!")
    private double peso;

    @NotNull
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(
            name = "id_tutor",
            nullable = false
    )
    @ManyToOne
    private Tutor tutor;

    @OneToMany(mappedBy = "animal")
    List<Agendamento> agendamentos = new ArrayList<>();

    public Animal(String raca, double peso, Tutor tutor) {
        this.raca = raca;
        this.peso = peso;
        this.tutor = tutor;
    }
}
