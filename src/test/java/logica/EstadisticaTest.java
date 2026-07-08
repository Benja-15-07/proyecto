package logica;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EstadisticaTest {

    @Test
    void agregarVictoria() {
        Estadistica estadistica = new Estadistica();

        estadistica.agregarVictoria();

        assertEquals(1, estadistica.getVictorias());
    }

    @Test
    void agregarDerrota() {
        Estadistica estadistica = new Estadistica();

        estadistica.agregarDerrota();

        assertEquals(1, estadistica.getDerrotas());
    }

    @Test
    void agregarEmpate() {
        Estadistica estadistica = new Estadistica();

        estadistica.agregarEmpate();

        assertEquals(1, estadistica.getEmpates());
    }

    @Test
    void getPartidasJugadas() {
        Estadistica estadistica = new Estadistica();

        estadistica.agregarVictoria();
        estadistica.agregarDerrota();
        estadistica.agregarDerrota();

        assertEquals(3, estadistica.getPartidasJugadas());
    }

    @Test
    void getPuntos() {
        Estadistica estadistica = new Estadistica();

        estadistica.agregarVictoria();
        estadistica.agregarDerrota();
        estadistica.agregarEmpate();

        assertEquals(4, estadistica.getPuntos());
    }
}