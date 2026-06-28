package com.gremio.gremios.DTO;

import lombok.Data;

@Data
public class MisionDTO {

    private Integer id;
    private String nombre;
    private String descripcion;
    private Integer nivel;
    private Integer expRecompensa;
    private Integer oroRecompensa;
    private Boolean estado;
    private String nombreGremio;
}
