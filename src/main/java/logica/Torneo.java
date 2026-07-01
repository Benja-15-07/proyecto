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
    }

    public void addParticipante(Participante participante){
        participantes.add(participante);
    }

    public void registrarResultado(Enfrentamiento enfrentamiento,int puntaje1, int puntaje2){
        enfrentamiento.registrarResultado(criterio, puntaje1, puntaje2);
    }

    public void generarEnfrentamiento(){
        enfrentamientos = formato.generarEnfrentamientos(participantes);
    }
}
