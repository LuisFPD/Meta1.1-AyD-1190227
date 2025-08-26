package Paquete;

//Clase que se encarga de tomar los datos de las personas
public class Persona {

    private int id;
    private String nombre;


    public Persona(String nombre) {
        this.nombre = nombre;

    }

    public Persona(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;

    }

    // Getters y Setters
    public int getId() {
        return id;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    @Override
    public String toString() {
        return nombre; // útil para mostrar en ListView o ComboBox
    }

}
