package com.party.parties.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReputacionDTO {
    
    @Schema(example = "1")
    private Integer id;
    
    @Schema(example = "Exaltado")
    private String nombre;
    
    @Schema(example = "10")
    private Integer nivel;
    
    @Schema(example = "La Alianza del Norte")
    private String nombreFaccion;
    
    @Schema(example = "2")
    private Integer faccionId;
}
