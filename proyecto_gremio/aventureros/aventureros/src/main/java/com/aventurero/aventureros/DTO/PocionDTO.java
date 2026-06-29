package com.aventurero.aventureros.DTO;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PocionDTO {
    @Schema(example = "1")
    private Integer id;
    @Schema(example = "Pocion pequeña")
    private String nombre;
    @Schema(example = "Pocion que recupera una cantidad pequeña de salud")
    private String descripcion;

}