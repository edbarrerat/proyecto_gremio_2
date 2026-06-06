package com.aventurero.aventureros.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name= "armas")
public class Arma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank (message = "El nombre es obligatorio")
    @Size(min = 3, max = 40, message = "El nombre debe tener entre 3 y 40 caracteres")
    @Column(nullable = false, length = 40)
    private String nombre;

    @NotBlank (message = "La descripcion es obligatoria")
    @Size(min = 3, max = 100, message = "La descripcion debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String descripcion;

    @Builder.Default
    @Min(value = 1, message = "El daño mínimo es 1")
    @Max(value = 999, message = "El daño máximo es 999")
    @Column(nullable = false)
    private Integer dañoArma = 1;

//-----------------------------------------------------------------------------

    @OneToMany(mappedBy = "arma")
    @ToString.Exclude
    private List<Equipamiento> equipos;


}
