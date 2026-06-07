package com.gremio.gremios.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.gremio.gremios.DTO.MisionDTO;
import com.gremio.gremios.Model.Mision;
import com.gremio.gremios.Service.MisionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/misiones")
public class MisionController {

    @Autowired
    private MisionService misionService;

    @GetMapping
    public ResponseEntity<List<MisionDTO>> listarMisiones() {
        List<MisionDTO> misiones = misionService.obtenerTodos();
        return misiones.isEmpty() 
            ? new ResponseEntity<>(HttpStatus.NO_CONTENT) 
            : new ResponseEntity<>(misiones, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MisionDTO> crearMision(@Valid @RequestBody Mision mision) {
        return new ResponseEntity<>(misionService.guardarMision(mision), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MisionDTO> buscarPorId(@PathVariable Integer id) {
        try {
            MisionDTO mision = misionService.buscarPorId(id);
            return new ResponseEntity<>(mision, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mision> actualizarMision(@PathVariable Integer id, @Valid @RequestBody Mision mision){
        try{
            Mision newMision = misionService.actualizarMision(id, mision);
            return new ResponseEntity<>(newMision, HttpStatus.OK);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarMision(@PathVariable Integer id) {
        try {
            String resultado = misionService.eliminarMision(id);
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
