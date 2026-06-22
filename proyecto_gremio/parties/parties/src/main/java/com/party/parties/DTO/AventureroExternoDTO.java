package com.party.parties.DTO;

import java.util.List;

import lombok.Data;

@Data
public class AventureroExternoDTO {
    private Integer id;
    private Integer partyId;
    private String nombre;
    private String nombreParty;
    private String nombreProfesion;
    private List<String> nombrePociones;
    private List<String> nombreArmas;
}
