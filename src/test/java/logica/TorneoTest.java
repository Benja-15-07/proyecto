package logica;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TorneoTest {

    private Torneo torneo;

    @BeforeEach
    void setUp() {
        torneo = new Torneo(
                "Torneo",
                "Futbol",
                LocalDate.of(2026,1,1),
                new FormatoLiga(),
                new CriterioMayorPuntaje());
    }

    @Test
    void addParticipante() {
        Participante participante = new Individuo("A", "");

        torneo.addParticipante(participante);

        assertEquals(1, torneo.getParticipantes().size());
        assertEquals("A", torneo.getParticipantes().get(0).getNombre());
    }

    @Test
    void generarEnfrentamientos() {
        torneo.addParticipante(new Individuo("A",""));
        torneo.addParticipante(new Individuo("B",""));
        torneo.addParticipante(new Individuo("C",""));

        torneo.generarEnfrentamientos();

        assertEquals(3, torneo.getEnfrentamientos().size());
        assertNotNull(torneo.getCalendario());
    }

    @Test
    void generarSiguienteRonda() {
        torneo = new Torneo(
                "Torneo",
                "Futbol",
                LocalDate.of(2026,1,1),
                new FormatoEliminatoria(),
                new CriterioMayorPuntaje());

        Participante participante1 = new Individuo("A", "");
        Participante participante2 = new Individuo("B", "");
        Participante participante3 = new Individuo("C", "");
        Participante participante4 = new Individuo("D", "");

        torneo.addParticipante(participante1);
        torneo.addParticipante(participante2);
        torneo.addParticipante(participante3);
        torneo.addParticipante(participante4);

        torneo.generarEnfrentamientos();

        List<Participante> ganadores = new ArrayList<>();

        ganadores.add(participante1);
        ganadores.add(participante3);

        torneo.generarSiguienteRonda(ganadores);

        assertEquals(3, torneo.getEnfrentamientos().size());
        assertEquals(2, torneo.getRondaActual());
    }
}