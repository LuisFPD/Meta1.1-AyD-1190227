package Paquete;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TelefonosMariaDB {
    public static List<Telefono> getByPersonaId(int personaId) {
        //Nos permite obtener todos los telefonos de una persona
        List<Telefono> telefonos = new ArrayList<>();
        String sql = "SELECT * FROM Telefonos WHERE personaID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, personaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Telefono t = new Telefono(
                            rs.getInt("id"),
                            rs.getInt("personaID"),
                            rs.getString("telefono")
                    );
                    telefonos.add(t);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return telefonos;
    }
    public static void insert(Telefono t) {
        //Se encarga de insertar los telefonos de la persona en la base
        String sql = "INSERT INTO Telefonos (personaID, telefono) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, t.getPersonaId());
            ps.setString(2, t.getTelefono());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void deleteByPersonaId(int personaId) {
        //Se encarga de eliminar los telefonos de una perona borrada
        String sql = "DELETE FROM Telefonos WHERE personaId=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, personaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
