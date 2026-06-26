package com.aventurero.aventureros.DTO;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AventureroDTO {
    @Schema(example = "1")
    private Integer id;
    @Schema(example = "Frodo")
    private String nombre;
    @Schema(example = "1")
    private String nombreProfesion;
    @Schema(example = "1")
    private Integer party_id;
    @Schema(example="1")
    private  String nombre_party;
    @Schema(example = "Pocion pequeña")
    private List<String> nombrePociones;
    @Schema(example = "Sting")
    private List<String> nombreArmas;

}