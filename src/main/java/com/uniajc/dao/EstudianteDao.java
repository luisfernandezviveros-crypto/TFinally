package com.uniajc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.uniajc.config.ConexionPostgresDatabase;
import com.uniajc.modelo.Estudiante;

public class EstudianteDao {

    public void guardar(Estudiante estudiante) {
        String sql = "INSERT INTO \"practica-mvc\".estudiantes (name, lastname, email) VALUES (?, ?, ?)";

        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, estudiante.getNombre());
            pstmt.setString(2, estudiante.getApellido());
            pstmt.setString(3, estudiante.getEmail());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al guardar estudiante: " + e.getMessage());
            throw new RuntimeException("Error en base de datos", e);
        }
    }

    public List<Estudiante> obtenerTodos() {
        List<Estudiante> estudiantes = new ArrayList<>();
        String sql = "SELECT id, name, lastname, email FROM \"practica-mvc\".estudiantes";

        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Estudiante estudiante = mapearEstudiante(rs);
                estudiantes.add(estudiante);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar estudiantes: " + e.getMessage());
            throw new RuntimeException("Error en base de datos", e);
        }
        return estudiantes;
    }

    public Optional<Estudiante> buscarPorId(int id) {
        String sql = "SELECT id, name, lastname, email FROM \"practica-mvc\".estudiantes WHERE id = ?";

        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearEstudiante(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar estudiante por ID: " + e.getMessage());
            throw new RuntimeException("Error en base de datos", e);
        }
        return Optional.empty();
    }

    public Optional<Estudiante> buscarPorEmail(String email) {
        String sql = "SELECT id, name, lastname, email FROM \"practica-mvc\".estudiantes WHERE email = ?";

        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearEstudiante(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar estudiante por email: " + e.getMessage());
            throw new RuntimeException("Error en base de datos", e);
        }
        return Optional.empty();
    }

    public void actualizar(Estudiante estudiante) {
        String sql = "UPDATE \"practica-mvc\".estudiantes SET name = ?, lastname = ?, email = ? WHERE id = ?";

        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, estudiante.getNombre());
            pstmt.setString(2, estudiante.getApellido());
            pstmt.setString(3, estudiante.getEmail());
            pstmt.setInt(4, estudiante.getId());

            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new RuntimeException("Estudiante no encontrado con ID: " + estudiante.getId());
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar estudiante: " + e.getMessage());
            throw new RuntimeException("Error en base de datos", e);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM \"practica-mvc\".estudiantes WHERE id = ?";

        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new RuntimeException("Estudiante no encontrado con ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar estudiante: " + e.getMessage());
            throw new RuntimeException("Error en base de datos", e);
        }
    }

    private Estudiante mapearEstudiante(ResultSet rs) throws SQLException {
        Estudiante estudiante = new Estudiante();
        estudiante.setId(rs.getInt("id"));
        estudiante.setNombre(rs.getString("name"));
        estudiante.setApellido(rs.getString("lastname"));
        estudiante.setEmail(rs.getString("email"));
        return estudiante;
    }
}
