package com.petshop.petshop_api.dto.response.animal;

public record AnimalResponseDTO(Integer id,
                                String nome,
                                String raca,
                                double peso,
                                Integer idTutor) {
}
