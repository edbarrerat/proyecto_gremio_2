package com.aventurero.aventureros.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BolsoPocionesDTO {
    @Schema(example = "1")
    private Integer id;
    @Schema(example = "1")
    private Integer cantidad;
    @Schema(example = "Frodo")
    private String nombresAventureros;
    @Schema(example = "Pocion pequeña")
    private String nombresPociones;


}