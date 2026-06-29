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

import com.aventurero.aventureros.DTO.ProfesionDTO;
import com.aventurero.aventureros.model.Profesion;
import com.aventurero.aventureros.repository.ProfesionRepository;
import com.aventurero.aventureros.service.ProfesionService;

@ExtendWith(MockitoExtension.class)
public class ProfesionServiceTest {
    @Mock
    ProfesionRepository profesionRepository;

    @InjectMocks
    ProfesionService profesionService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testObtenerTodas_ListaVacia(){
        when(profesionRepository.findAll()).thenReturn(List.of());

        List<ProfesionDTO> resultado = profesionService.obtenerTodos();

        assertNotNull(resultado, "La lista devuelta por el servicio nunca debería ser nula");
        assertTrue(resultado.isEmpty(), "La lista de DTOs debería estar vacía");
        assertEquals(0, resultado.size(), "El tamaño de la lista resultante debe ser cero");

        verify(profesionRepository, times(1)).findAll();
        verifyNoMoreInteractions(profesionRepository);

    }

    @Test
    void testBuscarPorId_Exitoso(){
        Integer idExistente = 3;
        String nombreProfesion = "Paladín";
        String descripcionProfesion = "Guerrero sagrado que protege a sus aliados con auras y curaciones.";
    
    
        Profesion profesionEnBD = new Profesion();
        profesionEnBD.setId(idExistente);
        profesionEnBD.setNombre(nombreProfesion);
        profesionEnBD.setDescripcion(descripcionProfesion);
    
        when(profesionRepository.findById(idExistente)).thenReturn(Optional.of(profesionEnBD));

        ProfesionDTO resultado = profesionService.buscarPorId(idExistente);

        assertNotNull(resultado, "El DTO resultante no debería ser nulo");
        assertEquals(idExistente, resultado.getId(), "El ID en el DTO debe coincidir con el solicitado");
        assertEquals(nombreProfesion, resultado.getNombre(), "El nombre en el DTO debe coincidir con el de la BD");
        assertEquals(descripcionProfesion, resultado.getDescripcion(), "La descripción en el DTO debe coincidir con la de la BD");

        verify(profesionRepository, times(1)).findById(idExistente);
        verifyNoMoreInteractions(profesionRepository);
    }

    @Test
    void testGuardarProfesion_Exitoso() {
        Integer idFalso = 25;
        String nombreFalso = "Explorador";
        String descripcionFalsa = "Experto en supervivencia, rastreo y combate a distancia.";

        Profesion profesionAEnviar = new Profesion();
        profesionAEnviar.setNombre(nombreFalso);
        profesionAEnviar.setDescripcion(descripcionFalsa);

        Profesion profesionGuardada = new Profesion();
        profesionGuardada.setId(idFalso);
        profesionGuardada.setNombre(nombreFalso);
        profesionGuardada.setDescripcion(descripcionFalsa);
    
        when(profesionRepository.save(any(Profesion.class))).thenReturn(profesionGuardada);

        ProfesionDTO resultado = profesionService.guardarProfesion(profesionAEnviar);

        assertNotNull(resultado, "El DTO devuelto no debería ser nulo");
        assertEquals(idFalso, resultado.getId(), "El ID asignado en el DTO debe coincidir con el de la BD");
        assertEquals(nombreFalso, resultado.getNombre(), "El nombre mapeado en el DTO debe ser el correcto");
        assertEquals(descripcionFalsa, resultado.getDescripcion(), "La descripción mapeada en el DTO debe ser la correcta");

        verify(profesionRepository, times(1)).save(any(Profesion.class));
        verifyNoMoreInteractions(profesionRepository);

    
    }

    @Test
    void testEliminarProfesion_FallidoIdNoExiste(){
        Integer idInexistente = 404;

        when(profesionRepository.findById(idInexistente)).thenReturn(Optional.empty());

        String resultadoTexto = profesionService.eliminar(idInexistente);

        String mensajeEsperado = "No es posible eliminar, profesion " + idInexistente + " no existe en sistema";
    
        assertNotNull(resultadoTexto, "El texto devuelto no debería ser nulo");
        assertEquals(mensajeEsperado, resultadoTexto, "El servicio debió capturar el error y retornar el mensaje correspondiente");
    
        verify(profesionRepository, times(1)).findById(idInexistente);
        verify(profesionRepository, never()).delete(any(Profesion.class));
        verifyNoMoreInteractions(profesionRepository);    
    }

    @Test
    void testActualizarProfesion_Exitoso() {
        Integer idExistente = 5;
        String nombreOriginal = "Mago";
        String descripcionOriginal = "Lanza hechizos básicos de fuego.";
        
        String nuevoNombre = "Archimago";
        String nuevaDescripcion = "Domina las artes arcanas elementales.";

        Profesion profesionEnBD = new Profesion();
        profesionEnBD.setId(idExistente);
        profesionEnBD.setNombre(nombreOriginal);
        profesionEnBD.setDescripcion(descripcionOriginal);

        Profesion datosNuevos = new Profesion();
        datosNuevos.setNombre(nuevoNombre);
        datosNuevos.setDescripcion(nuevaDescripcion);

        when(profesionRepository.findById(idExistente)).thenReturn(Optional.of(profesionEnBD));
        when(profesionRepository.save(any(Profesion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Profesion resultado = profesionService.actualizarProfesion(idExistente, datosNuevos);

        assertNotNull(resultado, "La profesión modificada no debería ser nula");
        assertEquals(idExistente, resultado.getId(), "El ID de la profesión debe mantenerse intacto");
        assertEquals(nuevoNombre, resultado.getNombre(), "El nombre de la profesión debió actualizarse");
        assertEquals(nuevaDescripcion, resultado.getDescripcion(), "La descripción debió actualizarse");

        verify(profesionRepository, times(1)).findById(idExistente);
        verify(profesionRepository, times(1)).save(any(Profesion.class));
        verifyNoMoreInteractions(profesionRepository);
    }


}
