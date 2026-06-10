package com.gremio.gremios.DTO;

import lombok.Data;

@Data
public class GremioDTO {

    private Integer id;
    private String nombre;
    private Integer oro;
    private String nombreFaccion;
    private PartyRegistradaDTO party;
}
