package com.party.parties.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Entidad espejo que representa una Misión")
public class MisionExternoDTO {
    
    @Schema(example = "1")
    private Integer id;
    
    @Schema(example = "3")
    private Integer rangoId;
    
    @Schema(example = "Caza de Dragones")
    private String nombre;
    
    @Schema(example = "Derrotar al dragón ancestral en la Montaña Solitaria")
    private String descripcion;
    
    @Schema(example = "20")
    private Integer nivel;
    
    @Schema(example = "5000")
    private Integer expRecompensa;
    
    @Schema(example = "1500")
    private Integer oroRecompensa;
    
    @Schema(example = "false")
    private Boolean estado;
    
    @Schema(example = "Maestro")
    private String rango;
}
