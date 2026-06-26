package com.aventurero.aventureros.DTO;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PartyExternaDTO {
    @Schema(example = "1")
    private Integer id;
    @Schema(example = "Comunidad del Anillo")
    private String nombre;
    @Schema(example = "1")
    private Integer nivel;

}
