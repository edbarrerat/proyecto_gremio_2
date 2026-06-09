package com.party.parties.Model;

import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "parties")
public class Party {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 3, max = 70, message = "La party debe tener al menos 3 aventureros")
    @Column(nullable = false, length = 70)
    private String nombre;

    @Builder.Default
    @Min(value = 1, message = "El nivel mínimo es 1")
    @Max(value = 99, message = "El nivel máximo es 99")
    @Column(nullable = false)
    private Integer nivel = 1;

   //-------------------------------------------------------------------------
   
   @OneToMany(mappedBy = "party")
   @ToString.Exclude
   private List<Aventurero> aventureros;

   @ManyToOne
   @JoinColumn(name = "gremio_id")
   private Gremio gremio;
}
