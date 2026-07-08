package logica;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClasificacionTest {

    @Test
     void ordenarPorPuntos(){
        Participante participante1 = new Individuo("A","");
        Participante participante2 = new Individuo("B","");

        participante1.getEstadistica().agregarVictoria();
        participante2.getEstadistica().agregarEmpate();

        List<Participante> participantes = new ArrayList<>();
        participantes.add(participante1);
        participantes.add(participante2);

        Clasificacion clasificacion = new Clasificacion();
        clasificacion.actualizar(participantes);

        List<Participante> resultados = clasificacion.getClasificacion();

        assertEquals("A", resultados.get(0).getNombre());
        assertEquals("B", resultados.get(1).getNombre());
    }
}