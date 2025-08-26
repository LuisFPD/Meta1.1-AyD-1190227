//Meta 1.2 Analisis y diseño de sistemas - Luis Fernando Prieto Duarte
package Paquete;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
public class CRUD extends Application {
    //Esta clase es la que habre la interfaz grafica

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/org/example/m11/interfaz.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load(), 1920, 1080);
        stage.setTitle("CRUD Personas y telefonos");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
