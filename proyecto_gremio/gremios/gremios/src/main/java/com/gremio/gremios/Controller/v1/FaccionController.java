package com.gremio.gremios.Controller.v1;

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

import com.gremio.gremios.DTO.FaccionDTO;
import com.gremio.gremios.Model.Faccion;
import com.gremio.gremios.Service.FaccionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/facciones")
public class FaccionController {

    @Autowired
    private FaccionService faccionService;

    @GetMapping
    public ResponseEntity<List<FaccionDTO>> listarFacciones() {
        List<FaccionDTO> facciones = faccionService.obtenerTodos();
        return facciones.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(facciones);
    }

    @PostMapping
    public ResponseEntity<FaccionDTO> crearFaccion(@Valid @RequestBody Faccion faccion) {
        return new ResponseEntity<>(faccionService.guardarFaccion(faccion), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FaccionDTO> buscarPorId(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(faccionService.buscarPorId(id), HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<FaccionDTO> actualizarFaccion(
            @PathVariable Integer id, @Valid @RequestBody Faccion faccion) {
        try {
            return ResponseEntity.ok(faccionService.actualizarFaccion(id, faccion));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarFaccion(@PathVariable Integer id) {
        String resultado = faccionService.eliminarFaccion(id);
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }
}
