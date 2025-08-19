package Paquete;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonasMariaDB {
    public static List<Persona> getAll() {
        //Obtiene los datos de la persona de la base de datos
        List<Persona> personas = new ArrayList<>();
        String sql = "SELECT * FROM Personas";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Persona p = new Persona(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("direccion")
                );
                personas.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return personas;
    }

    public static int insert(Persona persona) {
        //Inserta los datos de la persona en la base de datos
        String sql = "INSERT INTO Personas (nombre, direccion) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, persona.getNombre());
            ps.setString(2, persona.getDireccion());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }



    public static void update(Persona persona) {
        //Modifica los datos de la persona en la base de datos
        String sql = "UPDATE Personas SET nombre=?, direccion=? WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, persona.getNombre());
            ps.setString(2, persona.getDireccion());
            ps.setInt(3, persona.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(int id) {
        //Borra los datos de la persona de la base de datos
        String sql = "DELETE FROM Personas WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}