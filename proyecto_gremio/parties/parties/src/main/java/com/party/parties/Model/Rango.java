package com.party.parties.Model;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rangos")
public class Rango {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Builder.Default
    @Min(value = 1, message = "El nivel mínimo es 1")
    @Max(value = 15, message = "El nivel máximo es 15")
    @Column(nullable = false)
    private Integer nivel = 1;

    //----------------------------------------------------------------------

    @OneToMany(mappedBy = "rango")
    @ToString.Exclude
    private List<Mision> misiones;
}
