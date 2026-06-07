package com.gremio.gremios.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gremio.gremios.Model.Gremio;

@Repository
public interface GremioRepository extends JpaRepository<Gremio, Integer>{

}
