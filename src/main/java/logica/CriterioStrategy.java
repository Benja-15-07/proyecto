package logica;

/** Patrón de diseño Strategy para la elección del criterio del torneo
 * Al inscribir un resultado se calcula un ganador según los datos del Enfrentamiento
 * El criterio puede ser mayor puntaje, menor puntaje, o de forma binaria
 * (Para elegir un criterio de "menor tiempo", el tiempo es procesado como un valor numérico y analizado
 * bajo "menor tiempo")*/
public interface CriterioStrategy {

    /**
     * Evalua el resultado de un enfrentamiento y determina el ganador.
     *
     * @param enfrentamiento enfrentamiento a evaluar
     * @return participante ganador
     */
    public Participante evaluarResultado(Enfrentamiento enfrentamiento);

    /**
     * Devuelve el nombre del criterio.
     *
     * @return nombre del criterio
     */
    public String getNombreCriterio();
}