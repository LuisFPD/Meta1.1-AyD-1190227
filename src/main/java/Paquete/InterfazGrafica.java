package Paquete;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
//Clase encargada de darle función a la interfaz grafica
//Esta intefaz utiliza todos los métodos creados en esta clase
//I - Interface Segregation Principle (ISP)
//Otras clases de alto y bajo nivel dependen de la interfaz gráfica
//D - Dependency Inversion Principle (DIP)
public class InterfazGrafica {

    //Campos de entrada
    @FXML private TextField textoNombre;
    @FXML private TextField textoDirecciones;
    @FXML private TextField textoTelefonos;

    //Tabla de Personas que no alteraa a la superclase
    //L - Liskov Substitution Principle (LSP)
    @FXML private TableView<Persona> tablaPersonas;
    @FXML private TableColumn<Persona, Integer> columnaIdPersona;
    @FXML private TableColumn<Persona, String> columnaNombre;


    //Tabla de Direcciones que no altera a la superclase
    //L - Liskov Substitution Principle (LSP)
    @FXML private TableView<Direccion> tablaDirecciones;
    @FXML private TableColumn<Direccion, Integer> columnaIdDireccion;
    @FXML private TableColumn<Direccion, Integer> columnaPersonaIdDir; // ID Persona
    @FXML private TableColumn<Direccion, String> columnaDireccionDetalle;

    //Tabla de Teléfonos que no altera a la superclase
    //L - Liskov Substitution Principle (LSP)
    @FXML private TableView<Telefono> tablaTelefonos;
    @FXML private TableColumn<Telefono, Integer> columnaIdTelefono;
    @FXML private TableColumn<Telefono, Integer> columnaPersonaIdTel; // ID Persona
    @FXML private TableColumn<Telefono, String> columnaTelefono;

    //Listas observables que no altera a la superclase
    //L - Liskov Substitution Principle (LSP)
    private ObservableList<Persona> listaPersonas;
    private ObservableList<Direccion> listaDirecciones;
    private ObservableList<Telefono> listaTelefonos;

    @FXML
    public void initialize() {
        // ---------------- Tabla de Personas ----------------
        columnaIdPersona.setCellValueFactory(c ->
                new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()).asObject()
        );
        columnaNombre.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getNombre())
        );


        //Direcciones
        columnaIdDireccion.setCellValueFactory(c -> c.getValue().idProperty().asObject());
        columnaPersonaIdDir.setCellValueFactory(c -> c.getValue().personaIdProperty().asObject());
        columnaDireccionDetalle.setCellValueFactory(c -> c.getValue().direccionProperty());

        //Telefonos
        columnaIdTelefono.setCellValueFactory(c -> c.getValue().idProperty().asObject());
        columnaPersonaIdTel.setCellValueFactory(c -> c.getValue().personaIdProperty().asObject());
        columnaTelefono.setCellValueFactory(c -> c.getValue().telefonoProperty());

        //Personas
        tablaPersonas.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                cargarDirecciones(newSel.getId());
                cargarTelefonos(newSel.getId());
                textoNombre.setText(newSel.getNombre());

                //mostrar direcciones y teléfonos como texto separado por comas
                textoDirecciones.setText(String.join(",", listaDirecciones.stream().map(Direccion::getDireccion).toList()));
                textoTelefonos.setText(String.join(",", listaTelefonos.stream().map(Telefono::getTelefono).toList()));
            } else {
                tablaDirecciones.getItems().clear();
                tablaTelefonos.getItems().clear();
                textoNombre.clear();
                textoDirecciones.clear();
                textoTelefonos.clear();
            }
        });

        //Cargar personas al iniciar
        cargarPersonas();
    }

    private void cargarPersonas() {
        listaPersonas = FXCollections.observableArrayList(PersonasMariaDB.getAll());
        tablaPersonas.setItems(listaPersonas);
    }

    private void cargarDirecciones(int personaId) {
        listaDirecciones = FXCollections.observableArrayList(DireccionesMariaDB.getByPersonaId(personaId));
        tablaDirecciones.setItems(listaDirecciones);
    }

    private void cargarTelefonos(int personaId) {
        listaTelefonos = FXCollections.observableArrayList(TelefonosMariaDB.getByPersonaId(personaId));
        tablaTelefonos.setItems(listaTelefonos);
    }

    @FXML
    private void crearRegistro() {
        if (!textoNombre.getText().isEmpty()) {
            Persona persona = new Persona(textoNombre.getText());
            int personaId = PersonasMariaDB.insert(persona);

            // Guardar direcciones
            if (!textoDirecciones.getText().isEmpty()) {
                String[] dirs = textoDirecciones.getText().split(",");
                for (String d : dirs) {
                    String dir = d.trim();
                    if (!dir.isEmpty()) {
                        DireccionesMariaDB.insertForPersona(personaId, dir);
                    }
                }
            }

            //Guardamos los telefonos
            if (!textoTelefonos.getText().isEmpty()) {
                String[] tels = textoTelefonos.getText().split(",");
                for (String t : tels) {
                    String tel = t.trim();
                    if (!tel.isEmpty()) {
                        TelefonosMariaDB.insert(new Telefono(personaId, tel));
                    }
                }
            }

            cargarPersonas();
            textoNombre.clear();
            textoDirecciones.clear();
            textoTelefonos.clear();
        }
    }

    @FXML
    private void modificarRegistro() {
        Persona persona = tablaPersonas.getSelectionModel().getSelectedItem();
        if (persona != null) {
            persona.setNombre(textoNombre.getText());
            PersonasMariaDB.update(persona);

            DireccionesMariaDB.deleteByPersonaId(persona.getId());
            if (!textoDirecciones.getText().isEmpty()) {
                String[] dirs = textoDirecciones.getText().split(",");
                for (String d : dirs) {
                    String dir = d.trim();
                    if (!dir.isEmpty()) {
                        DireccionesMariaDB.insertForPersona(persona.getId(), dir);
                    }
                }
            }

            TelefonosMariaDB.deleteByPersonaId(persona.getId());
            if (!textoTelefonos.getText().isEmpty()) {
                String[] tels = textoTelefonos.getText().split(",");
                for (String t : tels) {
                    String tel = t.trim();
                    if (!tel.isEmpty()) {
                        TelefonosMariaDB.insert(new Telefono(persona.getId(), tel));
                    }
                }
            }

            cargarPersonas();
            cargarDirecciones(persona.getId());
            cargarTelefonos(persona.getId());
        }
    }

    @FXML
    private void borrarRegistro() {
        Persona persona = tablaPersonas.getSelectionModel().getSelectedItem();
        if (persona != null) {
            DireccionesMariaDB.deleteByPersonaId(persona.getId());
            TelefonosMariaDB.deleteByPersonaId(persona.getId());
            PersonasMariaDB.delete(persona.getId());

            cargarPersonas();
            tablaDirecciones.getItems().clear();
            tablaTelefonos.getItems().clear();
            textoNombre.clear();
            textoDirecciones.clear();
            textoTelefonos.clear();
        }
    }
}