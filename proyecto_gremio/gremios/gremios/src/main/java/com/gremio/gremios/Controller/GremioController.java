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

import com.gremio.gremios.DTO.GremioDTO;
import com.gremio.gremios.DTO.MisionDTO;
import com.gremio.gremios.Model.Gremio;
import com.gremio.gremios.Service.GremioService;
import com.gremio.gremios.Service.MisionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/gremios")
public class GremioController {

    @Autowired
    private GremioService gremioService;

    @Autowired
    private MisionService misionService;

    @GetMapping
    public ResponseEntity<List<GremioDTO>> listarGremios() {
        List<GremioDTO> gremios = gremioService.obtenerTodos();
        return gremios.isEmpty() 
            ? new ResponseEntity<>(HttpStatus.NO_CONTENT) 
            : new ResponseEntity<>(gremios, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GremioDTO> crearGremio(@Valid @RequestBody Gremio gremio) {
        return new ResponseEntity<>(gremioService.guardarGremio(gremio), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GremioDTO> buscarPorId(@PathVariable Integer id) {
        try {
            GremioDTO gremio = gremioService.buscarPorId(id);
            return new ResponseEntity<>(gremio, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{gremioId}/party/{partyId}")
    public ResponseEntity<String> reclutarParty(@PathVariable Integer gremioId, @PathVariable Integer partyId) {
        try {
            String resultado = gremioService.añadirPartyAGremio(gremioId, partyId);
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Gremio> actualizarGremio(@PathVariable Integer id, @Valid @RequestBody Gremio gremio){
        try{
            Gremio newGremio = gremioService.actualizarGremio(id, gremio);
            return new ResponseEntity<>(newGremio, HttpStatus.OK);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{gremioId}/party/{partyId}")
    public ResponseEntity<String> expulsarParty(@PathVariable Integer gremioId, @PathVariable Integer partyId) {
        try {
            String resultado = gremioService.eliminarParty(gremioId, partyId);
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{gremioId}/mision/{misionId}")
    public ResponseEntity<String> agregarMisionAGremio(@PathVariable Integer gremioId, @PathVariable Integer misionId) {
        try {
            String resultado = gremioService.añadirMisionAGremio(gremioId, misionId);
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{gremioId}/mision/{misionId}")
    public ResponseEntity<String> misionCompletada(@PathVariable Integer gremioId, @PathVariable Integer misionId) {
        try {
            String resultado = gremioService.misionCompletada(gremioId, misionId);
            return new ResponseEntity<>(resultado,HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{gremioId}/faccion/{faccionId}")
    public ResponseEntity<String> asignarFaccion(@PathVariable Integer gremioId, @PathVariable Integer faccionId) {
        try {
            String resultado = gremioService.asignarFaccion(gremioId, faccionId);
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{gremioId}/faccion/{faccionId}")
    public ResponseEntity<String> desligarFaccion(@PathVariable Integer gremioId, @PathVariable Integer faccionId) {
        try {
            String resultado = gremioService.desligarFaccion(gremioId, faccionId);
            return new ResponseEntity<>(resultado,HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{gremioId}/misiones/completadas")
    public ResponseEntity<List<MisionDTO>> misionesCompletadas(@PathVariable Integer gremioId) {
        try {
            List<MisionDTO> misiones = misionService.obtenerMisionesCompletadas(gremioId);
            return misiones.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(misiones, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
