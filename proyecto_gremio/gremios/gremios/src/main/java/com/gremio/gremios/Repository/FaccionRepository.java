package com.gremio.gremios.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gremio.gremios.Model.Faccion;

@Repository
public interface FaccionRepository extends JpaRepository <Faccion, Integer>{

}
