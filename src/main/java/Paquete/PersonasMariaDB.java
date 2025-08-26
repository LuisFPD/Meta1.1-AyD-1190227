package Paquete;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Clase que se encarga de conectar la clase persona con la base
public class PersonasMariaDB {

    private static final String URL = "jdbc:mariadb://localhost:3307/agenda";
    private static final String USER = "usuario1";
    private static final String PASSWORD = "superpassword";

    //Obtener todas las personas
    public static List<Persona> getAll() {
        List<Persona> lista = new ArrayList<>();
        String sql = "SELECT * FROM Personas";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");

                //Traer direcciones de la persona
                List<Direccion> dirs = DireccionesMariaDB.getByPersonaId(id);
                List<String> direcciones = new ArrayList<>();
                for (Direccion d : dirs) {
                    direcciones.add(d.getDireccion());
                }

                lista.add(new Persona(id, nombre));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    //Insertar persona y devolver su ID generado
    public static int insert(Persona p) {
        String sql = "INSERT INTO Personas (nombre) VALUES (?)";
        int idGenerado = -1;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getNombre());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    idGenerado = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idGenerado;
    }

    //Actualizar persona
    public static void update(Persona p) {
        String sql = "UPDATE Personas SET nombre = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Borrar persona
    public static void delete(int id) {
        String sql = "DELETE FROM Personas WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
