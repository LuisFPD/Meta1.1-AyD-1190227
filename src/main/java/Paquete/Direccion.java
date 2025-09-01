package Paquete;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
//Clase que unicamente gestiona los datos de las direcciones con el fundamento
//Single Responsibility Principle (SRP)
public class Direccion {
    //Se utilizan private final para asegurar que los valores sean cerrados a modificaciones
    //O - Open/Closed Principle (OCP)
    private final IntegerProperty id;
    private final IntegerProperty personaId;
    private final StringProperty direccion;

    public Direccion(int id, int personaId, String direccion) {
        this.id = new SimpleIntegerProperty(id);
        this.personaId = new SimpleIntegerProperty(personaId);
        this.direccion = new SimpleStringProperty(direccion);
    }

    public String getDireccion() { return direccion.get(); }

    // Propiedades (para binding si quieres)
    public IntegerProperty idProperty() { return id; }
    public IntegerProperty personaIdProperty() { return personaId; }
    public StringProperty direccionProperty() { return direccion; }

}

