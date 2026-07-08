package logica;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FormatoEliminatoriaTest {

    @Test
    void generarEnfrentamientos(){
        List<Participante> participantes = new ArrayList<>();

        participantes.add(new Individuo("A", ""));
        participantes.add(new Individuo("B", ""));
        participantes.add(new Individuo("C", ""));
        participantes.add(new Individuo("D", ""));

        FormatoEliminatoria formato = new FormatoEliminatoria();

        List<Enfrentamiento> enfrentamientos = formato.generarEnfrentamientos(participantes);

        assertEquals(2, enfrentamientos.size());

        assertEquals("A", enfrentamientos.get(0).getParticipante1().getNombre());
        assertEquals("B", enfrentamientos.get(0).getParticipante2().getNombre());
        assertEquals("C", enfrentamientos.get(1).getParticipante1().getNombre());
        assertEquals("D", enfrentamientos.get(1).getParticipante2().getNombre());
    }

    @Test
    void cantidadParticipantesNoPotenciaDeDos(){
        List<Participante> participantes = new ArrayList<>();

        participantes.add(new Individuo("A", ""));
        participantes.add(new Individuo("B", ""));
        participantes.add(new Individuo("C", ""));

        FormatoEliminatoria formato = new FormatoEliminatoria();

        assertThrows(IllegalArgumentException.class, () -> formato.generarEnfrentamientos(participantes));
    }

    @Test
    void generarBracket(){
        List<Participante> participantes = new ArrayList<>();

        participantes.add(new Individuo("A", ""));
        participantes.add(new Individuo("B", ""));
        participantes.add(new Individuo("C", ""));
        participantes.add(new Individuo("D", ""));

        FormatoEliminatoria formato = new FormatoEliminatoria();

        List<Enfrentamiento> enfrentamientos = formato.generarEnfrentamientos(participantes);

        Bracket bracket = formato.generarBracket(enfrentamientos);

        assertNotNull(bracket);
    }

    @Test
    void calcularFechaFin(){
        FormatoEliminatoria formato = new FormatoEliminatoria();

        int participantes = 4;

        LocalDate fechaInicio = LocalDate.of(2026, 1, 1);
        LocalDate fechaEsperada = fechaInicio.plusDays(participantes - 2);

        assertEquals(fechaEsperada, formato.calcularFechaFin(fechaInicio, participantes));
    }
}