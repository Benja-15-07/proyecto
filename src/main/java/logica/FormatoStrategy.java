package logica;

import java.time.LocalDate;
import java.util.ArrayList;

/** Patrón de diseño Strategy para la elección del formato del torneo
 * A partir de la lista de participantes, se generan enfrentamientos con un formato de ligas o de eliminatoria
 * Es decir, todos los participantes se enfrentan una vez con cada otro participante
 * o se escojen enfrentamientos en parejas, respectivamente.*/
public interface FormatoStrategy {
    public ArrayList<Enfrentamiento> generarEnfrentamientos(ArrayList<Participante> participantes);

    public Bracket generarBracket(ArrayList<Enfrentamiento> enfrentamientos);

    public LocalDate calcularFechaFin(LocalDate fechaInicio, int participantes);

    public String getNombreFormato();
}
