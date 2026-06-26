package com.aventurero.aventureros.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

import com.aventurero.aventureros.DTO.ArmaDTO;
import com.aventurero.aventureros.model.Arma;
import com.aventurero.aventureros.repository.ArmaRepository;
import com.aventurero.aventureros.service.ArmaService;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
public class ArmaServiceTest {

    @Mock
    private ArmaRepository armaRepository;

    @InjectMocks
    private ArmaService armaService;

    private Faker faker = new Faker();
    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodas_Exitoso(){
        String nombreArma = faker.options().option(
            "Espada corta","Daga pequeña", "Bastón con Gema pequeña");
        String nombreArma2 = faker.options().option(
            "Espada corta","Daga pequeña", "Bastón con Gema pequeña");

        Arma armaFalsa = new Arma();
        armaFalsa.setId(1);
        armaFalsa.setNombre(nombreArma);
        armaFalsa.setDescripcion(faker.lorem().sentence(5));
        armaFalsa.setDañoArma(faker.number().numberBetween(1,999));

        Arma armaFalsa2 = new Arma();
        armaFalsa2.setId(2);
        armaFalsa2.setNombre(nombreArma2);
        armaFalsa2.setDescripcion(faker.lorem().sentence(5));
        armaFalsa2.setDañoArma(faker.number().numberBetween(1,999));

        List<Arma> listaArmaFalsa = List.of(armaFalsa,armaFalsa2);

        when(armaRepository.findAll()).thenReturn(listaArmaFalsa);

        List<ArmaDTO> resultado = armaService.obtenerTodas();

        assertNotNull(resultado, "La lista devuelta no debería ser Nula");
        assertEquals(2, resultado.size(),"Debería retornar dos armas");

        verify(armaRepository, times(1)).findAll();
        verifyNoMoreInteractions(armaRepository);


    }

    @Test
    void testBuscarPorId_Fallido(){
        Integer idFalso = 99;

        when(armaRepository.findById(idFalso)).thenReturn(Optional.empty());
        
        RuntimeException excepcion = assertThrows(RuntimeException.class, () ->{
            armaService.buscarPorId(idFalso);
        });
        assertEquals("Esta arma no existe.",excepcion.getMessage());

        verify(armaRepository, times(1)).findById(idFalso);
        verifyNoMoreInteractions(armaRepository);
    }

    @Test
    void testGuardarArma_Exitoso(){
        String nombreArmaFalso = faker.options().option(
            "Espada corta","Daga pequeña", "Bastón con Gema pequeña");
        Integer idFalso = faker.number().numberBetween(1,99);
        Integer dañoFalso = faker.number().numberBetween(1, 999);
        String descrFalsa = faker.lorem().sentence(5);
        
        Arma armaEnviada = new Arma();
        armaEnviada.setNombre(nombreArmaFalso);
        armaEnviada.setDescripcion(descrFalsa);
        armaEnviada.setDañoArma(dañoFalso);

        Arma armaFalsa = new Arma();
        armaFalsa.setId(idFalso);
        armaFalsa.setNombre(nombreArmaFalso);
        armaFalsa.setDescripcion(descrFalsa);
        armaFalsa.setDañoArma(dañoFalso);

        when(armaRepository.save(any(Arma.class))).thenReturn(armaFalsa);

        ArmaDTO resultado = armaService.guardarArma(armaEnviada);

        assertNotNull(resultado, "El DTO resultante no deberia ser nulo.");
        assertEquals(idFalso, resultado.getId(), "El ID generado debe coincidir.");
        assertEquals(nombreArmaFalso, resultado.getNombre(), "El nombre mapeado debe ser el correcto.");

        verify(armaRepository, times(1)).save(any(Arma.class));
        
    }

    @Test
    void eliminarArma_FalloPorIdInexistente(){
        Integer idFalso = 89;
        when(armaRepository.findById(idFalso)).thenReturn(Optional.empty());

        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            armaService.eliminarArma(idFalso);
        });

        assertEquals("No se puede eliminar: el arma con Id "+idFalso+" no existe.", excepcion.getMessage());
        verify(armaRepository, times(1)).findById(idFalso);
        verify(armaRepository, times(0)).delete(any(Arma.class));
    }

    @Test
void testActualizarArma_Exitoso() {
    Integer idExistente = 15;
    
    Arma armaEnBD = new Arma();
    armaEnBD.setId(idExistente);
    armaEnBD.setNombre("Espada Vieja");
    armaEnBD.setDescripcion("Una espada oxidada por el tiempo.");
    armaEnBD.setDañoArma(10);

    String nuevoNombre = faker.options().option("Excalibur", "Filo del Infinito", "Devoradora");
    String nuevaDescripcion = faker.lorem().sentence(4);
    Integer nuevoDaño = faker.number().numberBetween(100, 500);

    Arma armaConCambios = new Arma();
    armaConCambios.setNombre(nuevoNombre);
    armaConCambios.setDescripcion(nuevaDescripcion);
    armaConCambios.setDañoArma(nuevoDaño);

    when(armaRepository.findById(idExistente)).thenReturn(Optional.of(armaEnBD));
    when(armaRepository.save(any(Arma.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Arma resultado = armaService.actualizarArma(idExistente, armaConCambios);

    assertNotNull(resultado, "El objeto actualizado no debería ser nulo");
    assertEquals(idExistente, resultado.getId(), "El ID debe seguir siendo el mismo");
    
    assertEquals(nuevoNombre, resultado.getNombre(), "El nombre debió actualizarse");
    assertEquals(nuevaDescripcion, resultado.getDescripcion(), "La descripción debió actualizarse");
    assertEquals(nuevoDaño, resultado.getDañoArma(), "El daño debió actualizarse");

    verify(armaRepository, times(1)).findById(idExistente);
    verify(armaRepository, times(1)).save(any(Arma.class));
    verifyNoMoreInteractions(armaRepository);
}




}
