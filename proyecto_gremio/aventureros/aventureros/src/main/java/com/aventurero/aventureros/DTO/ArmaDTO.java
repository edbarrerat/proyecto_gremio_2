package com.aventurero.aventureros.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ArmaDTO {

    @Schema(example = "1")
    private Integer id;
    @Schema(example = "Espada corta")
    private String nombre;
    @Schema(example = "Espada para principiantes.")
    private String descripcion;
    @Schema(example = "2")
    private Integer dañoArma;

}
