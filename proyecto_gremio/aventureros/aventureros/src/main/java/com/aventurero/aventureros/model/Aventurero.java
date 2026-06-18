package com.aventurero.aventureros.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@Table(name = "aventurteros")
public class Aventurero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank (message = "El nombre es obligatorio")
    @Size(min = 3, max = 30, message = "El nombre debe tener entre 3 y 30 caracteres")
    @Column(nullable = false, length = 30)
    private String nombre;

    //------------------------------------------------------------

    @OneToMany(mappedBy = "aventurero")
    @ToString.Exclude
    private List<BolsoPociones> pocionesObtenidas;

    @ManyToOne
    @JoinColumn(name = "profesion_id")
    private Profesion profesion;

    @OneToMany(mappedBy = "aventurero")
    @ToString.Exclude
    private List<Equipamiento> equipoEquipado;
}
