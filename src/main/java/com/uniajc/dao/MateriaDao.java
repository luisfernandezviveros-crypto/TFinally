package com.uniajc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.uniajc.config.ConexionPostgresDatabase;
import com.uniajc.modelo.Materia;

public class MateriaDao {

    public void guardar(Materia materia) {
        String sql = "INSERT INTO \"practica-mvc\".materias (nombre, codigo, creditos) VALUES (?, ?, ?)";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, materia.getNombre());
            pstmt.setString(2, materia.getCodigo());
            pstmt.setInt(3, materia.getCreditos());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos", e);
        }
    }

    public List<Materia> obtenerTodos() {
        List<Materia> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, codigo, creditos FROM \"practica-mvc\".materias";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos", e);
        }
        return lista;
    }

    public Optional<Materia> buscarPorId(int id) {
        String sql = "SELECT id, nombre, codigo, creditos FROM \"practica-mvc\".materias WHERE id = ?";
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

    public Optional<Materia> buscarPorCodigo(String codigo) {
        String sql = "SELECT id, nombre, codigo, creditos FROM \"practica-mvc\".materias WHERE codigo = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, codigo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos", e);
        }
        return Optional.empty();
    }

    public void actualizar(Materia materia) {
        String sql = "UPDATE \"practica-mvc\".materias SET nombre = ?, codigo = ?, creditos = ? WHERE id = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, materia.getNombre());
            pstmt.setString(2, materia.getCodigo());
            pstmt.setInt(3, materia.getCreditos());
            pstmt.setInt(4, materia.getId());
            int filas = pstmt.executeUpdate();
            if (filas == 0) throw new RuntimeException("Materia no encontrada con ID: " + materia.getId());
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos", e);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM \"practica-mvc\".materias WHERE id = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int filas = pstmt.executeUpdate();
            if (filas == 0) throw new RuntimeException("Materia no encontrada con ID: " + id);
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos", e);
        }
    }

    private Materia mapear(ResultSet rs) throws SQLException {
        Materia m = new Materia();
        m.setId(rs.getInt("id"));
        m.setNombre(rs.getString("nombre"));
        m.setCodigo(rs.getString("codigo"));
        m.setCreditos(rs.getInt("creditos"));
        return m;
    }
}
