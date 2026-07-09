package logica;

import java.time.LocalDate;
import java.util.List;

/** Patrón de diseño Strategy para la elección del formato del torneo
 * A partir de la lista de participantes, se generan enfrentamientos con un formato de ligas o de eliminatoria
 * Es decir, todos los participantes se enfrentan una vez con cada otro participante
 * o se escojen enfrentamientos en parejas, respectivamente.*/
public interface FormatoStrategy {

    /**
     * Genera los enfrentamientos del torneo según el formato.
     *
     * @param participantes lista de participantes del torneo
     * @return lista de enfrentamientos
     */
    public List<Enfrentamiento> generarEnfrentamientos(List<Participante> participantes);

    /**
     * Genera el bracket del torneo.
     *
     * @param enfrentamientos lista de enfrentamientos del torneo
     * @return bracket generado o null si el formato no utiliza bracket
     */
    public Bracket generarBracket(List<Enfrentamiento> enfrentamientos);

    public String getNombreFormato();
}
