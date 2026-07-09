package logica;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa el bracket de un torneo eliminatorio.
 */
public class Bracket {
    private List<Enfrentamiento> bracket;

    /**
     * Crea un bracket a partir de una lista de enfrentamientos.
     *
     * @param enfrentamientos lista de enfrentamientos que conforman el bracket
     */
    public Bracket(List<Enfrentamiento> enfrentamientos){
        this.bracket = new ArrayList<>(enfrentamientos);
    }

    /**
     * Devuelve una copia de los enfrentamientos del bracket.
     *
     * @return lista de los enfrentamientos del bracket
     */
    public List<Enfrentamiento> getBracket(){
        return new ArrayList<>(bracket);
    }
}
