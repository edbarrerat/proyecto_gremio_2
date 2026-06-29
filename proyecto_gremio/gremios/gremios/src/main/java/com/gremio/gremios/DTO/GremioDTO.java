package com.gremio.gremios.DTO;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GremioDTO {

    private Integer id;
    private String nombre;
    private Integer oro;
    private String nombreFaccion;
    private List<PartyDTO> parties;
    private List<MisionDTO> misiones;
}
