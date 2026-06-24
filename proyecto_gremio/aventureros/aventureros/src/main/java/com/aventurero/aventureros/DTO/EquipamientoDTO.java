package com.aventurero.aventureros.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EquipamientoDTO {
    @Schema(example = "1")
    private Integer id;
    @Schema(example = "Frodo")
    private String nombresAventureros;
    @Schema(example = "Sting")
    private String nombresArmas;

}