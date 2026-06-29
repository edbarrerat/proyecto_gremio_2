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

import com.aventurero.aventureros.DTO.EquipamientoDTO;
import com.aventurero.aventureros.model.Arma;
import com.aventurero.aventureros.model.Aventurero;
import com.aventurero.aventureros.model.Equipamiento;
import com.aventurero.aventureros.repository.ArmaRepository;
import com.aventurero.aventureros.repository.AventureroRepository;
import com.aventurero.aventureros.repository.EquipamientoRepository;
import com.aventurero.aventureros.service.EquipamientoService;


@ExtendWith(MockitoExtension.class)
public class EquipamientoServiceTest {
    @Mock
    private EquipamientoRepository equipamientoRepository;

    @Mock
    private ArmaRepository armaRepository;

    @Mock
    private AventureroRepository aventureroRepository;

    @InjectMocks
    private EquipamientoService equipamientoService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodo_ListaVacia(){
    when(equipamientoRepository.findAll()).thenReturn(List.of());

    List<EquipamientoDTO> resultado = equipamientoService.obtenerTodos();

    assertNotNull(resultado, "La lista devuelta por el servicio nunca debería ser nula");
    assertTrue(resultado.isEmpty(), "La lista de DTOs debería estar vacía");
    assertEquals(0, resultado.size(), "El tamaño de la lista resultante debe ser estrictamente cero");

    verify(equipamientoRepository, times(1)).findAll();
    verifyNoMoreInteractions(equipamientoRepository);

    }

    @Test
    void testGuardarEquipamiento_Exito(){
        Integer idFalso = 50;

        Aventurero aventureroFalso = new Aventurero();
        aventureroFalso.setId(1);
        aventureroFalso.setNombre("Thorin");

        Arma armaFalsa = new Arma();
        armaFalsa.setId(2);
        armaFalsa.setNombre("Orcrist");

        Equipamiento equipamientoAEnviar = new Equipamiento();
        equipamientoAEnviar.setAventurero(aventureroFalso);
        equipamientoAEnviar.setArma(armaFalsa);

        Equipamiento equipamientoGuardado = new Equipamiento();
        equipamientoGuardado.setId(idFalso);
        equipamientoGuardado.setAventurero(aventureroFalso);
        equipamientoGuardado.setArma(armaFalsa);

        when(equipamientoRepository.save(any(Equipamiento.class))).thenReturn(equipamientoGuardado);

        EquipamientoDTO resultado = equipamientoService.guardarEquipamiento(equipamientoAEnviar);

        assertNotNull(resultado);
        assertEquals(idFalso, resultado.getId());
        assertEquals("Thorin", resultado.getNombresAventureros());
        assertEquals("Orcrist", resultado.getNombresArmas());
    }

    @Test
    void testEliminarEquipamiento_FallidoIdEquipamientoNoExiste() {
        Integer idInexistente = 88;
        when(equipamientoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        String resultadoTexto = equipamientoService.eliminarEquipamiento(idInexistente);

        String mensajeEsperado = "No se puede eliminar: el Equipamiento con Id" + idInexistente + " no existe.";
    
        assertNotNull(resultadoTexto, "El texto de retorno no debería ser nulo");
        assertEquals(mensajeEsperado, resultadoTexto, "El servicio debió capturar el error y retornar el mensaje exacto");

        verify(equipamientoRepository, times(1)).findById(idInexistente);

        verify(equipamientoRepository, never()).delete(any(Equipamiento.class));
        verifyNoMoreInteractions(equipamientoRepository);
    }

    @Test
    void testAgregarArmaAlAventurero_Exitoso(){
        Integer aventureroId = 10;
        Integer armaId = 20;
        Integer equipamientoIdFalso = 500;

        String nombreAventurero = "Aragorn";
        String nombreArma = "Andúril";

        Aventurero aventureroFalso = new Aventurero();
        aventureroFalso.setId(aventureroId);
        aventureroFalso.setNombre(nombreAventurero);

        Arma armaFalsa = new Arma();
        armaFalsa.setId(armaId);
        armaFalsa.setNombre(nombreArma);

        Equipamiento equipamientoGuardado = Equipamiento.builder()
            .id(equipamientoIdFalso)
            .aventurero(aventureroFalso)
            .arma(armaFalsa)
            .build();
        
        when(aventureroRepository.findById(aventureroId)).thenReturn(Optional.of(aventureroFalso));
        when(armaRepository.findById(armaId)).thenReturn(Optional.of(armaFalsa));
        when(equipamientoRepository.save(any(Equipamiento.class))).thenReturn(equipamientoGuardado);

        EquipamientoDTO resultado = equipamientoService.agregarArmaAlAventurero(aventureroId, armaId);

        assertNotNull(resultado, "El DTO devuelto no debería ser nulo");
        assertEquals(equipamientoIdFalso, resultado.getId(), "El ID del equipamiento debe coincidir");

        assertEquals(nombreAventurero, resultado.getNombresAventureros(), "El nombre del aventurero en el DTO debe ser correcto");
        assertEquals(nombreArma, resultado.getNombresArmas(), "El nombre del arma en el DTO debe ser correcto");

        verify(aventureroRepository, times(1)).findById(aventureroId);
        verify(armaRepository, times(1)).findById(armaId);
        verify(equipamientoRepository, times(1)).save(any(Equipamiento.class));
        verifyNoMoreInteractions(aventureroRepository, armaRepository, equipamientoRepository);
    }

    


}
