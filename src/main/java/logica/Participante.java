package logica;

/**
 * Representa un participante de un torneo.
 */
public abstract class Participante {
    private static int contadorID = 1;

    private int id;
    private String nombre;
    private String contacto;

    private Estadistica estadistica;

    /**
     * Crea un participante.
     *
     * @param nombre nombre del participante
     * @param contacto contacto del participante
     */
    public Participante(String nombre, String contacto) {
        this.id = contadorID++;
        this.nombre = nombre;
        this.contacto = contacto;

        this.estadistica = new Estadistica();
    }

    /**
     * @return identificador único del participante
     */
    public int getID() {
        return id;
    }

    /**
     * @return nombre del participante
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return contacto del participante
     */
    public String getContacto() {
        return contacto;
    }

    /**
     * @return estadística del participante
     */
    public Estadistica getEstadistica() {
        return estadistica;
    }
}
