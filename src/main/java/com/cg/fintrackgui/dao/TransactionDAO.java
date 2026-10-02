package com.cg.fintrackgui.dao;

import com.cg.fintrackgui.model.Transaction;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

// Implementação do DAO para persistência de transações na base de dados SQLite
public class TransactionDAO implements IGenericDAO<Transaction> {

    // Insere um novo registo de transação na tabela
    @Override 
    public void save(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (id, name, value, type, date, month, description) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, transaction.getId());    
            stmt.setString(2, transaction.getName());
            stmt.setBigDecimal(3, transaction.getValue());
            stmt.setString(4, transaction.getType().trim());
            stmt.setString(5, transaction.getDate() != null ? transaction.getDate().toString() : null);
            stmt.setString(6, transaction.getRecurrencyMonth() != null ? transaction.getRecurrencyMonth().name() : null);
            stmt.setString(7, transaction.getDescription());

            stmt.executeUpdate();
        }
    }

    // Procura e retorna todas as transações registadas na base de dados
    @Override
    public List<Transaction> findAll() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT id, name, value, type, date, month, description FROM transactions";

        try (Connection conn = DbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));     
            }
        } 
        return transactions;
    } 
    
    // Procura uma transação específica através do seu ID único
    @Override 
    public Transaction findByID(String id) throws SQLException {
        String sql = "SELECT id, name, value, type, date, month, description FROM transactions WHERE id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTransaction(rs);                   
                }     
            } 
        }
        return null;
    }

    // Mapeia uma linha do resultado do SQL para um objeto Transaction
    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String name = rs.getString("name");
        BigDecimal value = rs.getBigDecimal("value");
        String type = rs.getString("type");
        String dateStr = rs.getString("date");
        LocalDate date = (dateStr != null && !dateStr.isEmpty()) ? LocalDate.parse(dateStr) : null;
        String monthStr = rs.getString("month");
        Month recurrencyMonth = monthStr != null ? Month.valueOf(monthStr) : null;
        String description = rs.getString("description");

        return new Transaction(id, name, value, type, date, recurrencyMonth, description);
    }

    // Atualiza os dados de uma transação existente com base no seu ID
    @Override
    public void update(Transaction transaction) throws SQLException {
        String sql = """
                     UPDATE transactions
                     SET name = ?, value = ?, type = ?, date = ?, month = ?, description = ? WHERE id = ?    
                     """;
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(7, transaction.getId());
            stmt.setString(1, transaction.getName());
            stmt.setBigDecimal(2, transaction.getValue());
            stmt.setString(3, transaction.getType().trim());
            stmt.setString(4, transaction.getDate() != null ? transaction.getDate().toString() : null);
            stmt.setString(5, transaction.getRecurrencyMonth() != null ? transaction.getRecurrencyMonth().name() : null);
            stmt.setString(6, transaction.getDescription());

            stmt.executeUpdate();
        }
    }

    // Remove o registo correspondente ao ID fornecido na tabela
    @Override
    public void remove(String id) throws SQLException {
        String sql = "DELETE FROM transactions WHERE id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                
            stmt.setString(1, id);

            stmt.executeUpdate();
        }
    }
}