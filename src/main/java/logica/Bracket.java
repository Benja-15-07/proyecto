package logica;

import java.util.ArrayList;

public class Bracket {
    private ArrayList<Enfrentamiento> bracket;

    public Bracket(ArrayList<Enfrentamiento> enfrentamientos){
        this.bracket = new ArrayList<>(enfrentamientos);
    }

    public ArrayList<Enfrentamiento> getBracket(){
        return new ArrayList<>(bracket);
    }
}
