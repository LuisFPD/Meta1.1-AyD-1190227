package Paquete;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class InterfazGrafica {

    @FXML private TextField textoNombre;
    @FXML private TextField textoDireccion;
    @FXML private TextField textoTelefonos;

    @FXML private TableView<Persona> tablaPersonas;
    @FXML private TableColumn<Persona, Integer> columnaIdPersona;
    @FXML private TableColumn<Persona, String> columnaNombre;
    @FXML private TableColumn<Persona, String> columnaDireccion;

    @FXML private TableView<Telefono> tablaTelefonos;
    @FXML private TableColumn<Telefono, Integer> columnaIdTelefono;
    @FXML private TableColumn<Telefono, Integer> columnaPersonaId;
    @FXML private TableColumn<Telefono, String> columnaTelefono;

    private ObservableList<Persona> listaPersonas;
    private ObservableList<Telefono> listaTelefonos;

    @FXML
    public void initialize() {
        //Obtenemos los datos de las tablas
        columnaIdPersona.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()).asObject());
        columnaNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombre()));
        columnaDireccion.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDireccion()));

        columnaIdTelefono.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()).asObject());
        columnaPersonaId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getPersonaId()).asObject());
        columnaTelefono.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTelefono()));

        tablaPersonas.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                cargarTelefonos(newSel.getId());
                //Aqui nos encargamos de separar los telefonos los cuales seran delimitados por comas ,
                String telefonosStr = String.join(",",
                        listaTelefonos.stream().map(Telefono::getTelefono).toList());
                textoTelefonos.setText(telefonosStr);
                textoNombre.setText(newSel.getNombre());
                textoDireccion.setText(newSel.getDireccion());
            } else {
                tablaTelefonos.getItems().clear();
                textoTelefonos.clear();
                textoNombre.clear();
                textoDireccion.clear();
            }
        });

        cargarPersonas();
    }

    private void cargarPersonas() {
        listaPersonas = FXCollections.observableArrayList(PersonasMariaDB.getAll());
        tablaPersonas.setItems(listaPersonas);
        tablaPersonas.refresh();
    }

    private void cargarTelefonos(int personaId) {
        listaTelefonos = FXCollections.observableArrayList(TelefonosMariaDB.getByPersonaId(personaId));
        tablaTelefonos.setItems(listaTelefonos);
        tablaTelefonos.refresh();
    }

    @FXML
    private void crearRegistro() {
        //Se encarga de registrar los datos de las personas en ambas tablas
        if (!textoNombre.getText().isEmpty() && !textoDireccion.getText().isEmpty()) {
            Persona persona = new Persona(textoNombre.getText(), textoDireccion.getText());
            int personaId = PersonasMariaDB.insert(persona);
            if (personaId > 0) {
                if (!textoTelefonos.getText().isEmpty()) {
                    String[] telefonos = textoTelefonos.getText().split(",");
                    for (String t : telefonos) {
                        String tel = t.trim();
                        if (!tel.isEmpty()) {
                            TelefonosMariaDB.insert(new Telefono(personaId, tel));
                        }
                    }
                }
                cargarPersonas();
                Persona nueva = listaPersonas.stream()
                        .filter(p -> p.getId() == personaId)
                        .findFirst()
                        .orElse(null);
                if (nueva != null) {
                    tablaPersonas.getSelectionModel().select(nueva);
                    cargarTelefonos(personaId);
                }
                textoNombre.clear();
                textoDireccion.clear();
                textoTelefonos.clear();
            }
        }
    }

    @FXML
    private void modificarRegistro() {
        //Se selecciona una persona y se modifican sus datos
        Persona persona = tablaPersonas.getSelectionModel().getSelectedItem();
        if (persona != null) {
            persona.setNombre(textoNombre.getText());
            persona.setDireccion(textoDireccion.getText());
            PersonasMariaDB.update(persona);
            TelefonosMariaDB.deleteByPersonaId(persona.getId());
            if (!textoTelefonos.getText().isEmpty()) {
                String[] telefonos = textoTelefonos.getText().split(",");
                for (String t : telefonos) {
                    String tel = t.trim();
                    if (!tel.isEmpty()) {
                        TelefonosMariaDB.insert(new Telefono(persona.getId(), tel));
                    }
                }
            }
            cargarPersonas();
            cargarTelefonos(persona.getId());
        }
    }

    @FXML
    private void borrarRegistro() {
        //Se limpian todos los datos de la persona y se eliminan de las tablas
        Persona persona = tablaPersonas.getSelectionModel().getSelectedItem();
        if (persona != null) {
            TelefonosMariaDB.deleteByPersonaId(persona.getId());
            PersonasMariaDB.delete(persona.getId());
            cargarPersonas();
            tablaTelefonos.getItems().clear();
            textoNombre.clear();
            textoDireccion.clear();
            textoTelefonos.clear();
        }
    }
}
