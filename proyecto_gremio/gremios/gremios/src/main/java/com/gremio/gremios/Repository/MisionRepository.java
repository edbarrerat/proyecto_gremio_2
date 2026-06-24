package com.gremio.gremios.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gremio.gremios.Model.Mision;

@Repository
public interface MisionRepository extends JpaRepository<Mision, Integer>{

    @Query("SELECT x FROM Mision x WHERE x.gremio.id = :gremioId AND x.estado = true")
    List<Mision> findMisionesCompletadas(@Param("gremioId") Integer gremioId);

    List<Mision> findByGremioIdAndEstadoTrue(Integer gremioId);
}
