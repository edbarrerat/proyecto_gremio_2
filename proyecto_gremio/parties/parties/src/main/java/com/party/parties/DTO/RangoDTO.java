package com.party.parties.DTO;

import lombok.Data;

@Data
public class RangoDTO {
    private Integer id;
    private String nombre;
    private Integer nivel;
    private MisionExternoDTO mision;
}