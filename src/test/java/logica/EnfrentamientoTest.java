package logica;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnfrentamientoTest {

    @Test
    void registrarResultado() {
        Participante participante1 = new Individuo("A","");
        Participante participante2 = new Individuo("B","");

        Enfrentamiento enfrentamiento = new Enfrentamiento(participante1, participante2);

        enfrentamiento.registrarResultado(new CriterioMayorPuntaje(), 5, 3);

        assertTrue(enfrentamiento.estadoFinalizado());
        assertEquals(participante1, enfrentamiento.getGanador());

        assertEquals(1, participante1.getEstadistica().getVictorias());
        assertEquals(1, participante2.getEstadistica().getDerrotas());

        assertEquals(3, participante1.getEstadistica().getPuntos());
        assertEquals(0, participante2.getEstadistica().getPuntos());
    }

    @Test
    void registrarResultadoDosVeces(){
        Participante participante1 = new Individuo("A","");
        Participante participante2 = new Individuo("B","");

        Enfrentamiento enfrentamiento = new Enfrentamiento(participante1, participante2);

        enfrentamiento.registrarResultado(new CriterioMayorPuntaje(), 5, 3);

        assertThrows(IllegalStateException.class, () -> enfrentamiento.registrarResultado(new CriterioMayorPuntaje(), 1, 4));
    }
}