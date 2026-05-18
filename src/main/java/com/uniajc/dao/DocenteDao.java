package com.uniajc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.uniajc.config.ConexionPostgresDatabase;
import com.uniajc.modelo.Docente;

public class DocenteDao {

    public void guardar(Docente docente) {
        String sql = "INSERT INTO \"practica-mvc\".docentes (name, lastname, email, especialidad) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, docente.getNombre());
            pstmt.setString(2, docente.getApellido());
            pstmt.setString(3, docente.getEmail());
            pstmt.setString(4, docente.getEspecialidad());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al guardar docente: " + e.getMessage());
            throw new RuntimeException("Error en base de datos", e);
        }
    }

    public List<Docente> obtenerTodos() {
        List<Docente> docentes = new ArrayList<>();
        String sql = "SELECT id, name, lastname, email, especialidad FROM \"practica-mvc\".docentes";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                docentes.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar docentes: " + e.getMessage());
            throw new RuntimeException("Error en base de datos", e);
        }
        return docentes;
    }

    public Optional<Docente> buscarPorId(int id) {
        String sql = "SELECT id, name, lastname, email, especialidad FROM \"practica-mvc\".docentes WHERE id = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos", e);
        }
        return Optional.empty();
    }

    public Optional<Docente> buscarPorEmail(String email) {
        String sql = "SELECT id, name, lastname, email, especialidad FROM \"practica-mvc\".docentes WHERE email = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos", e);
        }
        return Optional.empty();
    }

    public void actualizar(Docente docente) {
        String sql = "UPDATE \"practica-mvc\".docentes SET name = ?, lastname = ?, email = ?, especialidad = ? WHERE id = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, docente.getNombre());
            pstmt.setString(2, docente.getApellido());
            pstmt.setString(3, docente.getEmail());
            pstmt.setString(4, docente.getEspecialidad());
            pstmt.setInt(5, docente.getId());
            int filas = pstmt.executeUpdate();
            if (filas == 0) throw new RuntimeException("Docente no encontrado con ID: " + docente.getId());
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos", e);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM \"practica-mvc\".docentes WHERE id = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int filas = pstmt.executeUpdate();
            if (filas == 0) throw new RuntimeException("Docente no encontrado con ID: " + id);
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos", e);
        }
    }

    private Docente mapear(ResultSet rs) throws SQLException {
        Docente d = new Docente();
        d.setId(rs.getInt("id"));
        d.setNombre(rs.getString("name"));
        d.setApellido(rs.getString("lastname"));
        d.setEmail(rs.getString("email"));
        d.setEspecialidad(rs.getString("especialidad"));
        return d;
    }
}
