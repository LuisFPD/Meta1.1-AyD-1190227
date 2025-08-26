package Paquete;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
//Nueva clase que se encarga de conectar las direcciones con la base de datos
public class DireccionesMariaDB {

    private static final String URL = "jdbc:mariadb://localhost:3307/agenda";
    private static final String USER = "usuario1";
    private static final String PASSWORD = "superpassword";

    //Obtenemos las direcciones de una persona
    public static List<Direccion> getByPersonaId(int personaId) {
        List<Direccion> lista = new ArrayList<>();
        String sql = "SELECT id, direccion FROM Direcciones WHERE personaId = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, personaId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String direccion = rs.getString("direccion");
                lista.add(new Direccion(id, personaId, direccion));
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    //Agregamos una dirección para una persona
    public static void insertForPersona(int personaId, String direccion) {
        String sqlInsertDir = "INSERT INTO Direcciones (personaId, direccion) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sqlInsertDir, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, personaId);
            ps.setString(2, direccion);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Eliminamos todas las direcciones asociadas a una persona
    public static void deleteByPersonaId(int personaId) {
        String sql = "DELETE FROM Direcciones WHERE personaId = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, personaId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
