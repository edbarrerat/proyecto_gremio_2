package com.gremio.gremios.DTO;

import lombok.Data;

@Data
public class FaccionDTO {
    
    private Integer id;
    private String nombre;
    private String descripcion;
    private Boolean hostilidad;
    private String nombreGremio;
}
