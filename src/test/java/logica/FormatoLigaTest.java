package logica;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FormatoLigaTest {

    @Test
    void generarEnfrentamientos(){
        List<Participante> participantes = new ArrayList<>();

        participantes.add(new Individuo("A", ""));
        participantes.add(new Individuo("B", ""));
        participantes.add(new Individuo("C", ""));

        FormatoLiga formato = new FormatoLiga();

        List<Enfrentamiento> enfrentamientos = formato.generarEnfrentamientos(participantes);

        assertEquals(3, enfrentamientos.size());

        assertEquals("A", enfrentamientos.get(0).getParticipante1().getNombre());
        assertEquals("B", enfrentamientos.get(0).getParticipante2().getNombre());
        assertEquals("A", enfrentamientos.get(1).getParticipante1().getNombre());
        assertEquals("C", enfrentamientos.get(1).getParticipante2().getNombre());
        assertEquals("B", enfrentamientos.get(2).getParticipante1().getNombre());
        assertEquals("C", enfrentamientos.get(2).getParticipante2().getNombre());
    }

    @Test
    void cantidadParticipantesMenorADos(){
        List<Participante> participantes = new ArrayList<>();

        participantes.add(new Individuo("A", ""));

        FormatoLiga formato = new FormatoLiga();

        assertThrows(IllegalArgumentException.class, () -> formato.generarEnfrentamientos(participantes));
    }

    @Test
    void generarBracket(){
        List<Participante> participantes = new ArrayList<>();

        participantes.add(new Individuo("A", ""));
        participantes.add(new Individuo("B", ""));
        participantes.add(new Individuo("C", ""));

        FormatoLiga formato = new FormatoLiga();

        List<Enfrentamiento> enfrentamientos = formato.generarEnfrentamientos(participantes);

        Bracket bracket = formato.generarBracket(enfrentamientos);

        assertNull(bracket);
    }

    @Test
    void calcularFechaFin(){
        FormatoLiga formato = new FormatoLiga();

        int participantes = 4;
        int dias = participantes * (participantes - 1) / 2;

        LocalDate fechaInicio = LocalDate.of(2026, 1, 1);
        LocalDate fechaEsperada = fechaInicio.plusDays(dias - 1);

        assertEquals(fechaEsperada, formato.calcularFechaFin(fechaInicio, participantes));
    }
}