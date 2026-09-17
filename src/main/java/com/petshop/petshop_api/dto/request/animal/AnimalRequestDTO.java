package com.petshop.petshop_api.dto.request.animal;

public record AnimalRequestDTO(
                               String nome,
                               String raca,
                               double peso,
                               Integer idTutor) {
}
