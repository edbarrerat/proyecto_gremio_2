package com.aventurero.aventureros.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AventureroArmadoDTO {

    @Schema(example = "Frodo Baggins")
    private String nombreAventurero;
    @Schema(example = "Sting")
    private String nombreArma;

}
