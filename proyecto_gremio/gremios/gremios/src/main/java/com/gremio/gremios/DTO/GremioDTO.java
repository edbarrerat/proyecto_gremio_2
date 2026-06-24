package com.gremio.gremios.DTO;

import java.util.List;

import lombok.Data;

@Data
public class GremioDTO {

    private Integer id;
    private String nombre;
    private Integer oro;
    private String nombreFaccion;
    private List<PartyDTO> parties;
    private List<MisionDTO> misiones;
}
