package com.aventurero.aventureros.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "equipamiento")
public class Equipamiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//--------------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name = "aventurero_id")
    private Aventurero aventurero;

    @ManyToOne
    @JoinColumn(name = "arma_id")
    private Arma arma;

}
