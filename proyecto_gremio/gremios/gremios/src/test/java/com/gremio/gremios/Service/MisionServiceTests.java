package com.gremio.gremios.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gremio.gremios.DTO.MisionDTO;
import com.gremio.gremios.Model.Gremio;
import com.gremio.gremios.Model.Mision;
import com.gremio.gremios.Repository.MisionRepository;

@ExtendWith(MockitoExtension.class)
public class MisionServiceTests {

    @Mock
    private MisionRepository misionRepository;

    @InjectMocks
    private MisionService misionService;

    private Mision mision;
    private Gremio gremio;

    @BeforeEach
    void crearMision() {
        gremio = Gremio.builder().id(1).nombre("Pokemon").oro(1500).build();
        mision = Mision.builder()
                .id(1)
                .nombre("Atrapar un mitico")
                .descripcion("Necesitamos a alguien que atrape a un pokemon mitico, ofrecemos buena recompensa por ello")
                .nivel(5)
                .expRecompensa(100)
                .oroRecompensa(1500)
                .estado(false)
                .gremio(gremio)
                .build();
    }

    @Test
    void buscarPorId_cuandoExiste_retornaDTO() {
        when(misionRepository.findById(1)).thenReturn(Optional.of(mision));

        MisionDTO prueba = misionService.buscarPorId(1);

        assertNotNull(prueba);
        assertEquals("Atrapar un mitico", prueba.getNombre());
        assertEquals(5, prueba.getNivel());
        assertFalse(prueba.getEstado());
    }

    @Test
    void buscarPorId_siNoExiste_devuelveError() {
        when(misionRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> misionService.buscarPorId(99));
    }

    @Test
    void actualizarMision_actualizaSoloCamposNoNulos() {
        Mision cambios = new Mision();
        cambios.setNivel(10);
        cambios.setOroRecompensa(2000);

        when(misionRepository.findById(1)).thenReturn(Optional.of(mision));
        when(misionRepository.save(any(Mision.class))).thenReturn(mision);

        misionService.actualizarMision(1, cambios);

        assertEquals(10, mision.getNivel());
        assertEquals(2000, mision.getOroRecompensa());
        assertEquals("Atrapar un mitico", mision.getNombre());
    }

    @Test
    void eliminarMision_siExiste_eliminaYretornaMensaje() {
        when(misionRepository.findById(1)).thenReturn(Optional.of(mision));

        String prueba = misionService.eliminarMision(1);

        verify(misionRepository, times(1)).delete(mision);
        assertTrue(prueba.contains("eliminada"));
    }

    @Test
    void aceptarMision_siFueAceptada_devuelveMensajeError() {
        mision.setEstado(true);
        when(misionRepository.findById(1)).thenReturn(Optional.of(mision));

        String prueba = misionService.aceptarMision(1, 1);

        assertEquals("Esta misión ya fue completada.", prueba);
    }

    @Test
    void obtenerMisionesCompletadas_devuelveSoloCompletadas() {
        Mision completada = Mision.builder()
                .id(2).nombre("Misión vieja").descripcion("Descripción de prueba de misión ya finalizada")
                .estado(true).gremio(gremio).nivel(1).expRecompensa(10).oroRecompensa(100).build();

        when(misionRepository.findByGremioIdAndEstadoTrue(1)).thenReturn(List.of(completada));

        List<MisionDTO> prueba = misionService.obtenerMisionesCompletadas(1);

        assertEquals(1, prueba.size());
        assertTrue(prueba.get(0).getEstado());
    }
}
