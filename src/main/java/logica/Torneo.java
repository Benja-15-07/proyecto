package logica;
import java.time.LocalDate;
import java.util.ArrayList;

public class Torneo {
    private String nombre;
    private String disciplina;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private FormatoStrategy formato;
    private CriterioStrategy criterio;

    private ArrayList<Participante> participantes;
    private ArrayList<Enfrentamiento> enfrentamientos;
    private Clasificacion clasificacion;
    private Calendario calendario;
    private Bracket bracket;

    private ArrayList<Observer> observers;

    public Torneo(String nombre, String disciplina, LocalDate fechaInicio, LocalDate fechaFin, FormatoStrategy formato, CriterioStrategy criterio) {
        this.nombre = nombre;
        this.disciplina = disciplina;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.formato = formato;
        this.criterio = criterio;
        this.participantes = new ArrayList<>();
        this.enfrentamientos = new ArrayList<>();
        this.clasificacion = new Clasificacion();
        this.calendario = new Calendario();
        this.bracket = null;

        this.observers = new ArrayList<>();
        this.observers.add(this.clasificacion); this.observers.add(this.calendario);
    }

    public void addParticipante(Participante participante){
        participantes.add(participante);
        actualizar();
    }

    public void registrarResultado(Enfrentamiento enfrentamiento,int puntaje1, int puntaje2){
        enfrentamiento.registrarResultado(criterio, puntaje1, puntaje2);
        actualizar();
    }

    public void generarEnfrentamiento(){
        enfrentamientos = formato.generarEnfrentamientos(participantes);
        actualizar();
    }

    public void actualizar(){
        for(Observer s : observers){
            s.actualizar();
        }
    }

    public ArrayList<Participante> getParticipantes() {
        return new ArrayList<>(participantes);
    }

    public ArrayList<Enfrentamiento> getEnfrentamientos() {
        return new ArrayList<>(enfrentamientos);
    }
}
