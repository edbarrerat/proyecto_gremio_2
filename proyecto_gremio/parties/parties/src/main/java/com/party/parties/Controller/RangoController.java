package com.party.parties.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.party.parties.DTO.RangoDTO;
import com.party.parties.Model.Rango;
import com.party.parties.Service.RangoService;
import com.party.parties.assemblers.RangoModelAssembler;

@RestController
@RequestMapping("/api/v1/rango")
public class RangoController {

    @Autowired
    private RangoService rangoService;
  
    @Autowired
    private RangoModelAssembler rangoModelAssembler;

    @GetMapping
    public ResponseEntity<List<RangoDTO>> todosLosRangos() {
        List<RangoDTO> rango = rangoService.obtenerTodos();
        if (rango.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(rango, HttpStatus.OK);
    }

@GetMapping("/{id}")
public ResponseEntity<EntityModel<RangoDTO>> buscarPorId(@PathVariable Integer id) {
    try {
        RangoDTO ran = rangoService.buscarPorId(id);
        return ResponseEntity.ok(rangoModelAssembler.toModel(ran));
    } catch (RuntimeException e) {
        return ResponseEntity.notFound().build();
    }
}

    @PostMapping
    public ResponseEntity<Rango> agregarRango(@RequestBody Rango ran) {
        try {
            Rango guardado = rangoService.guardarRango(ran);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rango> actualizarRango(@PathVariable Integer id, @RequestBody Rango ran){
        try{
            Rango newRan = rangoService.actualizarRango(id, ran);
            return new ResponseEntity<>(newRan, HttpStatus.OK);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarRango(@PathVariable Integer id) {
        String resultado = rangoService.eliminar(id);
        if (resultado.contains("exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }

}
