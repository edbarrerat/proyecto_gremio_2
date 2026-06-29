package com.aventurero.aventureros.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfesionDTO {
    @Schema(example = "1")
    private Integer id;
    @Schema(example = "Ladron")
    private String nombre;
    @Schema(example = "Sigilosos y ágiles los ladrones son un elemento básico en cualquier party")
    private String descripcion;
}
