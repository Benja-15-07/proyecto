package logica;

import java.util.ArrayList;
import java.util.List;

public class Bracket {
    private List<Enfrentamiento> bracket;

    public Bracket(List<Enfrentamiento> enfrentamientos){
        this.bracket = new ArrayList<>(enfrentamientos);
    }

    public List<Enfrentamiento> getBracket(){
        return new ArrayList<>(bracket);
    }
}
