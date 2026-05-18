package com.uniajc.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Clase de utilidad para gestionar la conexión a la base de datos siguiendo el patrón Factory.
 * En aplicaciones reales, aquí se configuraría un Connection Pool como HikariCP.
 */
public class ConexionPostgresDatabase {
    private static final Properties properties = new Properties();

    static {
        // Cargamos las propiedades una sola vez al inicio de la aplicación
        try (FileInputStream fis = new FileInputStream(new File("config.properties"))) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("CRITICAL: Failed to load config.properties. " + e.getMessage());
        }
    }

    /**
     * Retorna una NUEVA conexión para cada llamada.
     * Permite que el DAO gestione el ciclo de vida de la conexión mediante try-with-resources.
     */
    public static Connection getConnection() throws SQLException {
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");
        
        if (url == null || user == null) {
            throw new SQLException("Database configuration is missing or invalid.");
        }
        
        return DriverManager.getConnection(url, user, password);
    }

    // Ya no necesitamos un closeConnection() estático global, 
    // porque cada conexión devuelta es independiente y debe ser cerrada por quien la solicitó.
}
