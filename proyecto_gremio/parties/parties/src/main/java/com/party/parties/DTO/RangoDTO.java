package com.party.parties.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RangoDTO {
    
    @Schema(example = "1")
    private Integer id;
    
    @Schema(example = "Veterano")
    private String nombre;
    
    @Schema(example = "5")
    private Integer nivel;
    
    @Schema(description = "Misión asociada a este rango")
    private MisionExternoDTO mision;
}