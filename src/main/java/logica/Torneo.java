package logica;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un torneo.
 * Se encarga de la gestión de participantes, los enfrentamientos,
 * el calendario y la clasificación, según el formato y el
 * criterio seleccionados.
 */
public class Torneo {
    private String nombre;
    private String disciplina;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private FormatoStrategy formato;
    private CriterioStrategy criterio;
    private int partidosPorDia;

    private List<Enfrentamiento> enfrentamientos;
    private int rondaActual;

    private List<Participante> participantes;
    private Clasificacion clasificacion;
    private Calendario calendario;
    private Bracket bracket;

    private List<Observer> observers;

    /**
     * Crea un torneo.
     *
     * @param nombre nombre del torneo
     * @param disciplina disciplina del torneo
     * @param fechaInicio fecha de inicio del torneo
     * @param formato formato del torneo
     * @param criterio criterio utilizado para determinar los ganadores
     */
    public Torneo(String nombre, String disciplina, LocalDate fechaInicio, FormatoStrategy formato, CriterioStrategy criterio) {
        this(nombre, disciplina, fechaInicio, formato, criterio, 1);
    }

    public Torneo(String nombre, String disciplina, LocalDate fechaInicio, FormatoStrategy formato, CriterioStrategy criterio, int partidosPorDia) {
        this.nombre = nombre;
        this.disciplina = disciplina;
        this.fechaInicio = fechaInicio;
        this.fechaFin = null;
        this.formato = formato;
        this.criterio = criterio;
        this.partidosPorDia = Math.max(1, partidosPorDia);
        this.participantes = new ArrayList<>();
        this.enfrentamientos = new ArrayList<>();
        this.clasificacion = new Clasificacion();
        this.calendario = null;
        this.bracket = null;

        this.observers = new ArrayList<>();
        this.observers.add(this.clasificacion);
    }

    /**
     * Agrega participantes al torneo.
     *
     * @param participante participante que se agregará al torneo
     */
    public void addParticipante(Participante participante){
        participantes.add(participante);
        actualizar();
    }

    /**
     * Registra el resultado de un enfrentamiento y actualiza la clasificación.
     *
     * @param enfrentamiento enfrentamiento al que se le registrará un resultado
     * @param puntaje1 puntuación del primer participante
     * @param puntaje2 puntuación del segundo participante
     */
    public void registrarResultado(Enfrentamiento enfrentamiento,int puntaje1, int puntaje2){
        enfrentamiento.registrarResultado(criterio, puntaje1, puntaje2);
        actualizar();
    }

    /**
     * Genera los enfrentamientos iniciales del torneo, calcula la fecha
     * de finalización, crea el calendario y el bracket depende del formato.
     */
    public void generarEnfrentamientos(){
        enfrentamientos = formato.generarEnfrentamientos(participantes);
        this.rondaActual = 1;

        for(Enfrentamiento enf : enfrentamientos){
            enf.setRonda(rondaActual);
        }

        calendario = new Calendario(enfrentamientos, fechaInicio, partidosPorDia);
        fechaFin = calendario.getFechaFin();
        bracket = formato.generarBracket(enfrentamientos);

        actualizar();
    }

    /**
     * Genera la siguiente ronda de un torneo eliminatorio a partir
     * de los ganadores de la ronda anterior.
     *
     * @param ganadores ganadores de la ronda anterior
     */
    public void generarSiguienteRonda(List<Participante> ganadores){
        if(!(formato instanceof FormatoEliminatoria) || ganadores.size() <= 1) {
            return;
        }

        rondaActual++;

        List<Enfrentamiento> nuevaRonda = formato.generarEnfrentamientos(ganadores);

        for(Enfrentamiento enf : nuevaRonda){
            enf.setRonda(rondaActual);
        }

        enfrentamientos.addAll(nuevaRonda);

        calendario = new Calendario(enfrentamientos, fechaInicio, partidosPorDia);
        fechaFin = calendario.getFechaFin();
        bracket = formato.generarBracket(enfrentamientos);

        actualizar();
    }

    /**
     * Notifica a los observers para que se actualicen.
     */
    public void actualizar(){
        for(Observer s : observers){
            s.actualizar(participantes);
        }
    }

    /**
     * @return lista de participantes del torneo
     */
    public List<Participante> getParticipantes() {
        return new ArrayList<>(participantes);
    }

    /**
     * @return lista de enfrentamientos del torneo
     */
    public List<Enfrentamiento> getEnfrentamientos() {
        return new ArrayList<>(enfrentamientos);
    }

    /**
     * @return nombre del torneo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return disciplina del torneo
     */
    public String getDisciplina() {
        return disciplina;
    }

    /**
     * @return fecha de inicio del torneo
     */
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    /**
     * @return fecha de finalización del torneo
     */
    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void asignarFechaEnfrentamiento(Enfrentamiento enfrentamiento, LocalDate fecha) {
        if (fecha != null && fechaInicio != null && fecha.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha del enfrentamiento no puede ser anterior al inicio del torneo.");
        }
        if (fecha != null && fechaFin != null && fecha.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha del enfrentamiento no puede ser posterior al fin del torneo.");
        }
        if (calendario == null) {
            calendario = new Calendario(enfrentamientos);
        }
        calendario.asignarFecha(enfrentamiento, fecha);
    }

    /**
     * @return nombre del formato del torneo
     */
    public String getNombreFormato() {
        return formato.getNombreFormato();
    }

    /**
     * @return nombre del criterio del torneo
     */
    public String getNombreCriterio() {
        return criterio.getNombreCriterio();
    }

    /**
     * @return clasificación del torneo
     */
    public Clasificacion getClasificacion() {
        return clasificacion;
    }

    /**
     * @return calendario del torneo
     */
    public Calendario getCalendario() {
        return calendario;
    }

    /**
     * @return bracket del torneo
     */
    public Bracket getBracket() {
        return bracket;
    }

    /**
     * @return ronda actual del torneo
     */
    public int getRondaActual() {
        return rondaActual;
    }
}
