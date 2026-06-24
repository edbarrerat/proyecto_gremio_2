package com.gremio.gremios.DTO;

import lombok.Data;

@Data
public class PartyDTO {
    private Integer id;
    private String nombre;
    private Integer nivel;
    private Integer gremioId;
}
