package com.uniajc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.uniajc.config.ConexionPostgresDatabase;
import com.uniajc.modelo.Inscripcion;

public class InscripcionDao {

    public void guardar(Inscripcion inscripcion) {
        String sql = "INSERT INTO \"practica-mvc\".inscripciones (estudiante_id, grupo_id, fecha) VALUES (?, ?, ?)";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, inscripcion.getEstudianteId());
            pstmt.setInt(2, inscripcion.getGrupoId());
            pstmt.setString(3, inscripcion.getFecha());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos", e);
        }
    }

    public List<Inscripcion> obtenerTodos() {
        List<Inscripcion> lista = new ArrayList<>();
        String sql = "SELECT i.id, i.estudiante_id, i.grupo_id, i.fecha, " +
                     "CONCAT(e.name, ' ', e.lastname) AS estudiante_nombre, g.nombre AS grupo_nombre " +
                     "FROM \"practica-mvc\".inscripciones i " +
                     "LEFT JOIN \"practica-mvc\".estudiantes e ON i.estudiante_id = e.id " +
                     "LEFT JOIN \"practica-mvc\".grupos g ON i.grupo_id = g.id";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos", e);
        }
        return lista;
    }

    public Optional<Inscripcion> buscarPorId(int id) {
        String sql = "SELECT i.id, i.estudiante_id, i.grupo_id, i.fecha, " +
                     "CONCAT(e.name, ' ', e.lastname) AS estudiante_nombre, g.nombre AS grupo_nombre " +
                     "FROM \"practica-mvc\".inscripciones i " +
                     "LEFT JOIN \"practica-mvc\".estudiantes e ON i.estudiante_id = e.id " +
                     "LEFT JOIN \"practica-mvc\".grupos g ON i.grupo_id = g.id " +
                     "WHERE i.id = ?";
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

    public boolean existeInscripcion(int estudianteId, int grupoId) {
        String sql = "SELECT id FROM \"practica-mvc\".inscripciones WHERE estudiante_id = ? AND grupo_id = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, estudianteId);
            pstmt.setInt(2, grupoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos", e);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM \"practica-mvc\".inscripciones WHERE id = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int filas = pstmt.executeUpdate();
            if (filas == 0) throw new RuntimeException("Inscripción no encontrada con ID: " + id);
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos", e);
        }
    }

    private Inscripcion mapear(ResultSet rs) throws SQLException {
        Inscripcion i = new Inscripcion();
        i.setId(rs.getInt("id"));
        i.setEstudianteId(rs.getInt("estudiante_id"));
        i.setGrupoId(rs.getInt("grupo_id"));
        i.setFecha(rs.getString("fecha"));
        i.setEstudianteNombre(rs.getString("estudiante_nombre"));
        i.setGrupoNombre(rs.getString("grupo_nombre"));
        return i;
    }
}
