package com.party.parties.DTO;

import lombok.Data;

@Data
public class MisionExternoDTO {
    private Integer id;
    private Integer rangoId;
    private String nombre;
    private String descripcion;
    private Integer nivel;
    private Integer expRecompensa;
    private Integer oroRecompensa;
    private Boolean estado;
    private String rango;
}
