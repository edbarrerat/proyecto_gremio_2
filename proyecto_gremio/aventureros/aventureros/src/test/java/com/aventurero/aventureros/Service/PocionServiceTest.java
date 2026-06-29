package com.aventurero.aventureros.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.aventurero.aventureros.DTO.ArmaDTO;
import com.aventurero.aventureros.DTO.PocionDTO;
import com.aventurero.aventureros.model.Arma;
import com.aventurero.aventureros.model.Pocion;
import com.aventurero.aventureros.repository.PocionRepository;
import com.aventurero.aventureros.service.PocionService;

import net.datafaker.Faker;

public class PocionServiceTest {
        
    @Mock
    private PocionRepository pocionRepository;

    @InjectMocks
    private PocionService pocionService;

    private Faker faker = new Faker();
    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodas_Exitoso(){
        String nombrePocion = faker.options().option(
            "Pocion pequeña","Pocion mediana","Pocion grande");
        String nombrePocion2 = faker.options().option(
            "Pocion pequeña","Pocion mediana","Pocion grande");
        
        Pocion pocionFalsa1 = new Pocion();
        pocionFalsa1.setId(1);
        pocionFalsa1.setNombre(nombrePocion);
        pocionFalsa1.setDescripcion("Pocion que restaura una cantidad se salud según su tamaño");

        Pocion pocionFalsa2 = new Pocion();
        pocionFalsa2.setId(2);
        pocionFalsa2.setNombre(nombrePocion2);
        pocionFalsa2.setDescripcion("Pocion que restaura una cantidad de salud según su tamaño");

        List<Pocion> listaPocionFalsa = List.of(pocionFalsa1,pocionFalsa2);

        when(pocionRepository.findAll()).thenReturn(listaPocionFalsa);

        List<PocionDTO> resultado = pocionService.obtenerTodas();

        assertNotNull(resultado, "La lista devuelta no debería ser nula");
        assertEquals(2, resultado.size(),"Debería retornar dos pociones");

        verify(pocionRepository, times(1)).findAll();
        verifyNoMoreInteractions(pocionRepository);
    }

    @Test
    void testBuscarPorId_Fallido(){
        Integer idFalso = 99;

        when(pocionRepository.findById(idFalso)).thenReturn(Optional.empty());
        
        RuntimeException excepcion = assertThrows(RuntimeException.class, () ->{
            pocionService.buscarPorId(idFalso);
        });
        assertEquals("Esta pocion no existe.",excepcion.getMessage());

        verify(pocionRepository, times(1)).findById(idFalso);
        verifyNoMoreInteractions(pocionRepository);
    }

    @Test
    void testGuardarPocion_Exitoso(){
        Integer idFalso = 1;
        String nombreFalso = "Poción de Maná";
        String descripcionFalsa = "Restaura 50 puntos de maná instantáneamente.";
        
        Pocion pocionAEnviar = new Pocion();
        pocionAEnviar.setNombre(nombreFalso);
        pocionAEnviar.setDescripcion(descripcionFalsa);

        Pocion pocionGuardada = new Pocion();
        pocionGuardada.setId(idFalso);
        pocionGuardada.setNombre(nombreFalso);
        pocionGuardada.setDescripcion(descripcionFalsa);

        when(pocionRepository.save(any(Pocion.class))).thenReturn(pocionGuardada);

        PocionDTO resultado = pocionService.guardarPocion(pocionAEnviar);

        assertNotNull(resultado, "El DTO devuelto no debería ser nulo");
        assertEquals(idFalso, resultado.getId(), "El ID asignado por la base de datos debe coincidir");
        assertEquals(nombreFalso, resultado.getNombre(), "El nombre mapeado en el DTO debe ser el correcto");
        assertEquals(descripcionFalsa, resultado.getDescripcion(), "La descripción mapeada en el DTO debe ser la correcta");

        verify(pocionRepository, times(1)).save(any(Pocion.class));
        verifyNoMoreInteractions(pocionRepository);
    }
    
    @Test
    void testEliminarPocion_FallidoNoExisteId(){
        Integer idFalso = 999;

        when(pocionRepository.findById(idFalso)).thenReturn(Optional.empty());

        String resultado = pocionService.eliminarPocion(idFalso);

        String mensajeEsperado = "No se puede eliminar: la poción con Id" + idFalso + " no está existe.";
        assertNotNull(resultado, "El texto de retorno no debería ser nulo");
        assertEquals(mensajeEsperado, resultado, "El servicio debió capturar la excepción y retornar el texto exacto del error");

        verify(pocionRepository, times(1)).findById(idFalso);

        verify(pocionRepository, never()).delete(any(Pocion.class));
        verifyNoMoreInteractions(pocionRepository);

    }

    @Test
    void testActualizarPocion_Exitoso(){
        Integer idExistente = 1;
        
        Pocion pocionExistente = new Pocion();
        pocionExistente.setId(idExistente);
        pocionExistente.setNombre("Poción mediana");
        pocionExistente.setDescripcion("Restaura 50 PS");

        Pocion datosNuevos = new Pocion();
        datosNuevos.setNombre("Poción de grande");
        datosNuevos.setDescripcion("Restaura 100 PS");

        when(pocionRepository.findById(idExistente)).thenReturn(Optional.of(pocionExistente));
        when(pocionRepository.save(any(Pocion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PocionDTO resultado = pocionService.actualizarPocion(datosNuevos, idExistente);

        assertNotNull(resultado);
        assertEquals("Poción de Súper Vida", resultado.getNombre());
        assertEquals("Restaura 100 PS", resultado.getDescripcion());

        verify(pocionRepository, times(1)).findById(idExistente);
        verify(pocionRepository, times(1)).save(any(Pocion.class));

    }



}
