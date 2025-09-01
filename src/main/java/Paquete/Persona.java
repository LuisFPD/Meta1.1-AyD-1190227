package Paquete;

//Clase que unicamente gestiona los datos de las personas con el fundamento
//Single Responsibility Principle (SRP)
public class Persona {
    //Se utilizan private para asegurar que los valores sean cerrados a modificaciones
    //O - Open/Closed Principle (OCP)
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