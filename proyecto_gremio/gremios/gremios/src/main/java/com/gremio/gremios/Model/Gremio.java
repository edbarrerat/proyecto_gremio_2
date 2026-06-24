package com.gremio.gremios.Model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
@Table (name = "gremios")
public class Gremio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank (message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @Builder.Default
    @Min(value = 1, message = "La cantidad de oro mínima es 1")
    @Max(value = 999999999, message = "La cantidad de oro maxima es 999999999")
    @Column(nullable = false)
    private Integer oro = 1;

    /*--------------------------------------------------------*/
    
    @OneToOne
    @JoinColumn(name = "faccion_id")
    private Faccion faccion;
    
    @OneToMany(mappedBy = "gremio")
    @Builder.Default
    private List<Mision> misiones = new ArrayList<>();
}
