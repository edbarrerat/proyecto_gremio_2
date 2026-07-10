package com.party.parties.DTO;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PartyDTO {
    
    @Schema(description = "Identificador único", example = "1")
    private Integer id;
    
    @Schema(description = "Nombre de la Party", example = "La comunidad del anillo")
    private String nombre;
    
    @Schema(description = "Nivel promedio", example = "1")
    private Integer nivel;
    
    @Schema(description = "Lista de nombres de los aventureros miembros", example = "Frodo")
    private List<String> nombresAventureros;
    
    @Schema(description = "ID del gremio al que pertenecen", example = "1")
    private Integer gremioId;
    
    @Schema(description = "Información detallada del aventurero externo")
    private AventureroExternoDTO aventurero;
}
