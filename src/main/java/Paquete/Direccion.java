package Paquete;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
//Nueva clase la cual contiene los datos que contiene cada direccion de la tabla de direcciones
public class Direccion {

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

