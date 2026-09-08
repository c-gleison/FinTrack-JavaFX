package com.cg.fintrackgui.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnection {
    private static final String URL = "jdbc:sqlite:fintrack.db";

    public static Connection getConnection() throws SQLException {
        final Connection connection = DriverManager.getConnection(URL);
        createTableIfNotExists(connection);
        return connection;
    }

    private static void createTableIfNotExists(Connection connection) {
        String sql = """
                CREATE TABLE IF NOT EXISTS transactions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT,
                    description TEXT,
                    value DECIMAL(10,2),
                    type TEXT,
                    date DATE
                )
            """;

        try (Statement statement = connection.createStatement()){
            statement.execute(sql);
            
        } catch (SQLException e) {
            System.err.println("Error initializing the database: " + e.getMessage());
        }
    }
    
}
