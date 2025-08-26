package Paquete;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
//Clase que se encarga de guardar los datos de los telefonos
public class Telefono {

    private final IntegerProperty id;
    private final IntegerProperty personaId;
    private final StringProperty telefono;

    public Telefono(int id, int personaId, String telefono) {
        this.id = new SimpleIntegerProperty(id);
        this.personaId = new SimpleIntegerProperty(personaId);
        this.telefono = new SimpleStringProperty(telefono);
    }

    public Telefono(int personaId, String telefono) {
        this.id = new SimpleIntegerProperty(0);
        this.personaId = new SimpleIntegerProperty(personaId);
        this.telefono = new SimpleStringProperty(telefono);
    }

    public int getPersonaId() { return personaId.get(); }
    public String getTelefono() { return telefono.get(); }

    public IntegerProperty idProperty() { return id; }
    public IntegerProperty personaIdProperty() { return personaId; }
    public StringProperty telefonoProperty() { return telefono; }

}
