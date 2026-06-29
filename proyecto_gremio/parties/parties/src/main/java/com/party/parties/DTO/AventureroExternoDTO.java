package com.party.parties.DTO;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Entidad espejo que representa un Aventurero de otro microservicio")
public class AventureroExternoDTO {
    
    @Schema(example = "10")
    private Integer id;
    
    @Schema(example = "1")
    private Integer partyId;
    
    @Schema(example = "Aragorn")
    private String nombre;
    
    @Schema(example = "Comunidad del Anillo")
    private String nombreParty;
    
    @Schema(example = "Montaraz")
    private String nombreProfesion;
    
    @Schema(example = "Poción de Curación Mayor")
    private List<String> nombrePociones;
    
    @Schema(example = "Espada Andúril")
    private List<String> nombreArmas;
}
