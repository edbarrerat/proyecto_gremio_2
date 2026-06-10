package com.aventurero.aventureros.DTO;

import java.util.List;

import lombok.Data;

@Data
public class AventureroDTO {
    private Integer id;
    private String nombre;
    private Integer partyId;
    private String nombreParty;
    private String nombreProfesion;
    private List<String> nombrePociones;
    private List<String> nombreArmas;

}