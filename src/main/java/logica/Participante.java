package logica;

public abstract class Participante {
    private static int contadorID = 1;

    private int id;
    private String nombre;
    private String contacto;

    private Estadistica estadistica;

    public Participante(String nombre, String contacto) {
        this.id = contadorID++;
        this.nombre = nombre;
        this.contacto = contacto;

        this.estadistica = new Estadistica();
    }

    public int getID() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContacto() {
        return contacto;
    }

    public Estadistica getEstadistica() {
        return estadistica;
    }
}
