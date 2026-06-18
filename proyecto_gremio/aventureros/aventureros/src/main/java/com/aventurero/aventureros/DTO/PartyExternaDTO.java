package com.aventurero.aventureros.DTO;

import java.util.List;

import lombok.Data;

@Data
public class PartyExternaDTO {
    private Integer id;
    private String nombre;
    private Integer nivel;
    private List<String> nombresAventureros;

}
