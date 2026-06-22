package com.party.parties.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reputaciones")
public class Reputacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Builder.Default
    @Min(value = -10, message = "El nivel mínimo es -10")
    @Max(value = 10, message = "El nivel máximo es 10")
    @Column(nullable = false)
    private Integer nivel = 0;
    
    @NotNull(message = "La facción siempre debe estar")
    private Integer faccionId;
}