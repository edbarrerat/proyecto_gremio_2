package com.gremio.gremios.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "misiones")
public class Mision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank (message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank (message = "La descripcion es obligatorio")
    @Size(min = 50, max = 500, message = "La descripcion debe tener entre 50 y 500 caracteres")
    @Column(nullable = false, length = 500)
    private String descripcion;

    @Builder.Default
    @Min(value = 1, message = "El nivel de mision minimo es 1")
    @Max(value = 10, message = "El nivel de mision maximo es 10")
    @Column(nullable = false)
    private Integer nivel = 1;

    @Builder.Default
    @Min(value = 10, message = "La experiencia minima es 10")
    @Max(value = 1000000, message = "La experiencia maxima es 1.000.000")
    @Column(nullable = false)
    private Integer expRecompensa = 10;

    @Builder.Default
    @Min(value = 100, message = "La experiencia minima es 10")
    @Max(value = 1000000, message = "La experiencia maxima es 1.000.000")
    @Column(nullable = false)
    private Integer oroRecompensa = 100;

    @NotNull (message = "Debes definir si esta completa o no")
    @Column(nullable = false)
    private Boolean estado;

    //--------------------------------------------------------

    @ManyToOne
    @JoinColumn(name = "gremio_id")
    private Gremio gremio;

    @ManyToOne
    @JoinColumn(name = "faccion_id")
    private Faccion faccion;

    @ManyToOne
    @JoinColumn(name = "rango_id")
    private Rango rango;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;
}
