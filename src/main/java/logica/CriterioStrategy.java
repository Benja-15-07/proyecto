package logica;

/** Patrón de diseño Strategy para la elección del criterio del torneo
 * Al inscribir un resultado se calcula un ganador según los datos del Enfrentamiento
 * El criterio puede ser mayor puntaje, menor puntaje, o de forma binaria
 * (Para elegir un criterio de "menor tiempo", el tiempo es procesado como un valor numérico y analizado
 * bajo "menor tiempo")*/
public interface CriterioStrategy {
    public Participante evaluarResultado(Enfrentamiento enfrentamiento);
}