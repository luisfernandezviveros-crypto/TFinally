package com.uniajc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.uniajc.config.ConexionPostgresDatabase;
import com.uniajc.modelo.Grupo;

public class GrupoDao {

    public void guardar(Grupo grupo) {
        String sql = "INSERT INTO \"practica-mvc\".grupos (nombre, docente_id, materia_id) VALUES (?, ?, ?)";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, grupo.getNombre());
            pstmt.setInt(2, grupo.getDocenteId());
            pstmt.setInt(3, grupo.getMateriaId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos", e);
        }
    }

    public List<Grupo> obtenerTodos() {
        List<Grupo> lista = new ArrayList<>();
        String sql = "SELECT g.id, g.nombre, g.docente_id, g.materia_id, " +
                     "CONCAT(d.name, ' ', d.lastname) AS docente_nombre, m.nombre AS materia_nombre " +
                     "FROM \"practica-mvc\".grupos g " +
                     "LEFT JOIN \"practica-mvc\".docentes d ON g.docente_id = d.id " +
                     "LEFT JOIN \"practica-mvc\".materias m ON g.materia_id = m.id";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos", e);
        }
        return lista;
    }

    public Optional<Grupo> buscarPorId(int id) {
        String sql = "SELECT g.id, g.nombre, g.docente_id, g.materia_id, " +
                     "CONCAT(d.name, ' ', d.lastname) AS docente_nombre, m.nombre AS materia_nombre " +
                     "FROM \"practica-mvc\".grupos g " +
                     "LEFT JOIN \"practica-mvc\".docentes d ON g.docente_id = d.id " +
                     "LEFT JOIN \"practica-mvc\".materias m ON g.materia_id = m.id " +
                     "WHERE g.id = ?";
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

    public void actualizar(Grupo grupo) {
        String sql = "UPDATE \"practica-mvc\".grupos SET nombre = ?, docente_id = ?, materia_id = ? WHERE id = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, grupo.getNombre());
            pstmt.setInt(2, grupo.getDocenteId());
            pstmt.setInt(3, grupo.getMateriaId());
            pstmt.setInt(4, grupo.getId());
            int filas = pstmt.executeUpdate();
            if (filas == 0) throw new RuntimeException("Grupo no encontrado con ID: " + grupo.getId());
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos", e);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM \"practica-mvc\".grupos WHERE id = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int filas = pstmt.executeUpdate();
            if (filas == 0) throw new RuntimeException("Grupo no encontrado con ID: " + id);
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos", e);
        }
    }

    private Grupo mapear(ResultSet rs) throws SQLException {
        Grupo g = new Grupo();
        g.setId(rs.getInt("id"));
        g.setNombre(rs.getString("nombre"));
        g.setDocenteId(rs.getInt("docente_id"));
        g.setMateriaId(rs.getInt("materia_id"));
        g.setDocenteNombre(rs.getString("docente_nombre"));
        g.setMateriaNombre(rs.getString("materia_nombre"));
        return g;
    }
}
