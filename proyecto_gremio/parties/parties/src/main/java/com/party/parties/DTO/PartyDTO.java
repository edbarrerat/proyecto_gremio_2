package com.party.parties.DTO;

import java.util.List;

import lombok.Data;

@Data
public class PartyDTO {
    private Integer id;
    private String nombre;
    private Integer nivel;
    private List<String> nombresAventureros;
    private Integer gremioId;
}
