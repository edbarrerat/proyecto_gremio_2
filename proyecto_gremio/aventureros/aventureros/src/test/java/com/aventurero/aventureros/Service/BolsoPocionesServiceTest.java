package com.aventurero.aventureros.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aventurero.aventureros.DTO.BolsoPocionesDTO;
import com.aventurero.aventureros.model.Aventurero;
import com.aventurero.aventureros.model.BolsoPociones;
import com.aventurero.aventureros.model.Pocion;
import com.aventurero.aventureros.repository.BolsoPocionesRepository;
import com.aventurero.aventureros.service.BolsoPocionesService;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
public class BolsoPocionesServiceTest {
    
    @Mock
    private BolsoPocionesRepository bolsoPocionesRepository;

    @InjectMocks
    private BolsoPocionesService bolsoPocionesService;

    private Faker faker = new Faker();
    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodos_ListaVacia(){
        when(bolsoPocionesRepository.findAll()).thenReturn(List.of());

        List<BolsoPocionesDTO> resultado = bolsoPocionesService.obtenerTodos();
        assertNotNull(resultado, "La lista nunca debe ser nula");
        assertTrue(resultado.isEmpty(),"La lista debería venir completamente vacía");

        verify(bolsoPocionesRepository, times (1)).findAll();
    }

    @Test
    void testGuardarBolso_Exitoso(){
        Integer idFalso = 100;
        Integer cantidadFalsa = 3;
        String nombreAventurero = faker.lordOfTheRings().character();
        String nombrePocion = "Poción de Curación pequeñar";

        Aventurero aventureroFalso = new Aventurero();
        aventureroFalso.setId(1);
        aventureroFalso.setNombre(nombreAventurero);

        Pocion pocionFalsa = new Pocion();
        pocionFalsa.setId(2);
        pocionFalsa.setNombre(nombrePocion);

        BolsoPociones bolsoGuardado = new BolsoPociones();
        bolsoGuardado.setId(idFalso);
        bolsoGuardado.setCantidad(cantidadFalsa);
        bolsoGuardado.setAventurero(aventureroFalso);
        bolsoGuardado.setPocion(pocionFalsa);

        when(bolsoPocionesRepository.save(any(BolsoPociones.class))).thenReturn(bolsoGuardado);

        BolsoPocionesDTO resultado = bolsoPocionesService.guardarBolso(bolsoGuardado);

        assertNotNull(resultado, "El DTO resultante no debería ser nulo");
        assertEquals(idFalso, resultado.getId(), "El ID asignado por la BD debe coincidir");
        assertEquals(cantidadFalsa, resultado.getCantidad(), "La cantidad debe mapearse correctamente");
        
        verify(bolsoPocionesRepository, times(1)).save(any(BolsoPociones.class));
        verifyNoMoreInteractions(bolsoPocionesRepository);

    }

    @Test
    void eliminarBolsoPociones_Fallido(){
        Integer idFalso = 250;

        when(bolsoPocionesRepository.findById(idFalso)).thenReturn(Optional.empty());

        String resultadoTexto = bolsoPocionesService.eliminarBolso(idFalso);

        String mensajeEsperado = "No se puede eliminar: el Bolso de Pociones con Id" + idFalso + " no existe.";

        assertNotNull(resultadoTexto, "El resultado no debería ser nulo");
        assertEquals(mensajeEsperado, resultadoTexto, "El servicio debió capturar el error y retornar el mensaje de texto");

        verify(bolsoPocionesRepository, times(1)).findById(idFalso);

        verify(bolsoPocionesRepository, never()).delete(any(BolsoPociones.class));
        verifyNoMoreInteractions(bolsoPocionesRepository);
    }
    

}
