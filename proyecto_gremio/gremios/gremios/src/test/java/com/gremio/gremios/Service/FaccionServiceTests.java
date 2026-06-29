package com.gremio.gremios.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gremio.gremios.DTO.FaccionDTO;
import com.gremio.gremios.Model.Faccion;
import com.gremio.gremios.Model.Gremio;
import com.gremio.gremios.Repository.FaccionRepository;
import com.gremio.gremios.Repository.GremioRepository;


@ExtendWith(MockitoExtension.class)
public class FaccionServiceTests {

    @Mock
    private FaccionRepository faccionRepository;

    @Mock
    private GremioRepository gremioRepository;

    @InjectMocks
    private FaccionService faccionService;

    private Faccion faccion;

    @BeforeEach
    void crearFaccion() {
        faccion = Faccion.builder().id(1)
                .nombre("Elite Four")
                .descripcion("Los 4 mejores entrenadores de la región")
                .hostilidad(false)
                .build();
    }

    @Test
    void buscarPorId_siExiste_devuelveDTO() {
        when(faccionRepository.findById(1)).thenReturn(Optional.of(faccion));
        when(gremioRepository.findByFaccion(faccion)).thenReturn(Optional.empty());

        FaccionDTO prueba = faccionService.buscarPorId(1);

        assertNotNull(prueba);
        assertEquals("Elite Four", prueba.getNombre());
        assertFalse(prueba.getHostilidad());
    }

    @Test
    void buscarPorId_siNoExiste_devuelveError () {
        when(faccionRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> faccionService.buscarPorId(99));
    }

    @Test
    void eliminarFaccion_siTieneGremio_eliminaCorrectamente() {
        when(faccionRepository.findById(1)).thenReturn(Optional.of(faccion));
        when(gremioRepository.findByFaccion(faccion)).thenReturn(Optional.empty());

        String prueba = faccionService.eliminarFaccion(1);

        verify(faccionRepository, times(1)).delete(faccion);
        assertTrue(prueba.contains("eliminada"));
    }

    @Test
    void actualizarFaccion_cuandoExiste_actualizaSoloCamposNoNulos() {
        Faccion cambios = new Faccion();
        cambios.setNombre("Campeones Regionales");

        when(faccionRepository.findById(1)).thenReturn(Optional.of(faccion));
        when(faccionRepository.save(any(Faccion.class))).thenReturn(faccion);
        when(gremioRepository.findByFaccion(any())).thenReturn(Optional.empty());

        faccionService.actualizarFaccion(1, cambios);

        assertEquals("Campeones Regionales", faccion.getNombre());
        assertEquals("Los mejores entrenadores de todo el mundo", faccion.getDescripcion());
    }

    @Test
    void convertirADTO_cuandoTieneGremio_incluyeNombreGremio() {
        Gremio gremio = Gremio.builder().id(1).nombre("Pokemon").build();
        when(faccionRepository.findById(1)).thenReturn(Optional.of(faccion));
        when(gremioRepository.findByFaccion(faccion)).thenReturn(Optional.of(gremio));

        FaccionDTO prueba = faccionService.buscarPorId(1);

        assertEquals("Pokemon", prueba.getNombreGremio());
    }
}
