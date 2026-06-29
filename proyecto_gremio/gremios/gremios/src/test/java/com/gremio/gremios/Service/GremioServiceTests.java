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
import org.springframework.web.reactive.function.client.WebClient;

import com.gremio.gremios.DTO.GremioDTO;
import com.gremio.gremios.Model.Faccion;
import com.gremio.gremios.Model.Gremio;
import com.gremio.gremios.Model.Mision;
import com.gremio.gremios.Repository.FaccionRepository;
import com.gremio.gremios.Repository.GremioRepository;
import com.gremio.gremios.Repository.MisionRepository;


@ExtendWith(MockitoExtension.class)
public class GremioServiceTests {

    @Mock
    private GremioRepository gremioRepository;

    @Mock
    private MisionRepository misionRepository;

    @Mock
    private FaccionRepository faccionRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @InjectMocks
    private GremioService gremioService;

    private Gremio gremio;

    @BeforeEach
    void crearGremio() {
        gremio = Gremio.builder().id(1).nombre("Pokemon").oro(500).build();
    }

    @Test
    void buscarPorId_siExiste_retornaDTO() {
        when(gremioRepository.findById(1)).thenReturn(Optional.of(gremio));

        GremioDTO prueba = gremioService.buscarPorId(1);

        assertNotNull(prueba);
        assertEquals("Pokemon", prueba.getNombre());
        assertEquals(500, prueba.getOro());
    }

    @Test
    void buscarPorId_siNoExiste_sueltaError() {
        when(gremioRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> gremioService.buscarPorId(99));
    }

    @Test
    void guardarGremio_retornaDTOGuardado() {
        when(gremioRepository.save(gremio)).thenReturn(gremio);

        GremioDTO prueba = gremioService.guardarGremio(gremio);

        assertNotNull(prueba);
        assertEquals("Pokemon", prueba.getNombre());
        verify(gremioRepository, times(1)).save(gremio);
    }

    @Test
    void actualizarGremio_cuandoExiste_actualizaCampos() {
        Gremio gremioCambiado = Gremio.builder().nombre("Digimon").oro(1000).build();
        when(gremioRepository.findById(1)).thenReturn(Optional.of(gremio));
        when(gremioRepository.save(any(Gremio.class))).thenReturn(gremio);

        gremioService.actualizarGremio(1, gremioCambiado);

        assertEquals("Digimon", gremio.getNombre());
        assertEquals(1000, gremio.getOro());
    }

    @Test
    void misionCompletada_marcarComoCompleta() {
        Mision mision = Mision.builder().id(1).nombre("Atrapar un Palkia").estado(false).gremio(gremio).build();
        when(misionRepository.findById(1)).thenReturn(Optional.of(mision));

        String prueba = gremioService.misionCompletada(1, 1);

        assertTrue(mision.getEstado());
        assertEquals("¡La misión ha sido completada exitosamente!", prueba);
    }

    @Test
    void misionCompletada_devuelveError() {
        Gremio otroGremio = Gremio.builder().id(99).nombre("Otro").build();
        Mision mision = Mision.builder().id(1).nombre("Cazar dragones").estado(false).gremio(otroGremio).build();
        when(misionRepository.findById(1)).thenReturn(Optional.of(mision));

        String prueba = gremioService.misionCompletada(1, 1);

        assertFalse(mision.getEstado()); // no se modificó
        assertEquals("Esta misión no pertenece al gremio indicado.", prueba);
    }

    @Test
    void asignarFaccion_enGremioSinFaccion() {
        Faccion faccion = Faccion.builder().id(1).nombre("Elite Four").build();
        when(gremioRepository.findById(1)).thenReturn(Optional.of(gremio));
        when(faccionRepository.findById(1)).thenReturn(Optional.of(faccion));
        when(gremioRepository.save(any(Gremio.class))).thenReturn(gremio);

        String prueba = gremioService.asignarFaccion(1, 1);

        assertEquals(faccion, gremio.getFaccion());
        assertTrue(prueba.contains("Elite Four"));
    }

    @Test
    void asignarFaccion_siGremioTieneFaccion_devuelveError() {
        Faccion faccionExistente = Faccion.builder().id(2).nombre("Ya asignada").build();
        gremio.setFaccion(faccionExistente);
        when(gremioRepository.findById(1)).thenReturn(Optional.of(gremio));

        String prueba = gremioService.asignarFaccion(1, 1);

        assertTrue(prueba.contains("ya tiene una facción aliada"));
    }

    @Test
    void desligarFaccion_desligadoCorrectamente() {
        gremio.setFaccion(Faccion.builder().id(1).nombre("Elite Four").build());
        when(gremioRepository.findById(1)).thenReturn(Optional.of(gremio));
        when(gremioRepository.save(any(Gremio.class))).thenReturn(gremio);

        String prueba = gremioService.desligarFaccion(1);

        assertNull(gremio.getFaccion());
        assertTrue(prueba.contains("desligado"));
    }

    @Test
    void desligarFaccion_siNoHayFaccion_devuelveError() {
        when(gremioRepository.findById(1)).thenReturn(Optional.of(gremio));

        String prueba = gremioService.desligarFaccion(1);

        assertTrue(prueba.contains("no tiene ninguna facción"));
    }
}
