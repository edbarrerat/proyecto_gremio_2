package com.aventurero.aventureros.DTO;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArmaDTO extends RepresentationModel<ArmaDTO>{

    @Schema(example = "1")
    private Integer id;
    @Schema(example = "Espada corta")
    private String nombre;
    @Schema(example = "Espada para principiantes.")
    private String descripcion;
    @Schema(example = "2")
    private Integer dañoArma;

}
