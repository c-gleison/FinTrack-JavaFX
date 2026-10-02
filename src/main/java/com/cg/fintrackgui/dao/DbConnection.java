package com.cg.fintrackgui.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// Gestor de conexões com a base de dados SQLite
public class DbConnection {
    private static String URL = "jdbc:sqlite:fintrack.db";

    // Metodo para permitir mudar a URL do banco de dados
    public static void setUrl(String newUrl) {
        URL = newUrl;
    }

    // Estabelece a conexão com a base de dados e garante a existência das tabelas
    public static Connection getConnection() throws SQLException {
        final Connection connection = DriverManager.getConnection(URL);
        createTableIfNotExists(connection);
        return connection;
    }

    // Cria a tabela de transações caso ainda não exista na base de dados
    private static void createTableIfNotExists(Connection connection) {
        String sql = """
                CREATE TABLE IF NOT EXISTS transactions (
                    id TEXT,
                    name TEXT,
                    value DECIMAL(10,2),
                    type TEXT,
                    date DATE,
                    month TEXT,
                    description TEXT
                )
            """;

        try (Statement statement = connection.createStatement()){
            statement.execute(sql);
            
        } catch (SQLException e) {
            System.err.println("Erro ao inicializar a base de dados: " + e.getMessage());
        }
    }
}