public class States {
    private String nombre;
    private boolean esFinal;

    public States(String nombre, boolean esFinal) {
        this.nombre = nombre;
        this.esFinal = esFinal;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean esFinal() {
        return esFinal;
    }
    @Override
    public String toString() {
        return this.nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}