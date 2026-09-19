package com.cg.fintrackgui.dao;

import com.cg.fintrackgui.model.Transaction;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


//Implementação do DAO para persistência de transações no SQLite.
public class TransactionDAO implements IGenericDAO<Transaction> {

    // Insere um novo registro de transação na tabela do banco
    @Override 
    public void save(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (name, value, type, date, description) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, transaction.getName());
            stmt.setBigDecimal(2, transaction.getValue());
            stmt.setString(3, transaction.getType());
            stmt.setString(4, transaction.getDate() != null ? transaction.getDate().toString() : null);
            stmt.setString(5, transaction.getDescription());

            stmt.executeUpdate();
        }
    }

    // Busca e retorna todas as transações cadastradas no banco de dados
    @Override
    public List<Transaction> findAll() throws SQLException {

        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT id, name, description, value, type, date FROM transactions";

        try (Connection conn = DbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));     
            }
        } 
        return transactions;
    } 
    
    // Busca uma transação específica pelo seu ID único
    @Override 
    public Transaction findByID(int id) throws SQLException {

        String sql = "SELECT id, name, description, value, type, date FROM transactions WHERE id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTransaction(rs);                   
                }     
            } 
        }
        return null;
    }

    // Mapeia uma linha do ResultSet do SQL para um objeto Transaction
    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {

        int id = rs.getInt("id");
        String name = rs.getString("name");
        BigDecimal value = rs.getBigDecimal("value");
        String type = rs.getString("type");
        String dateStr = rs.getString("date");
            LocalDate date = (dateStr != null && !dateStr.isEmpty()) ? LocalDate.parse(dateStr) : null;
        String description = rs.getString("description");

        // Instancia usando a ordem: ID, Nome, Valor, Tipo, Data, Descrição
        return new Transaction(id, name, value, type, date, description);
    }

    // Atualiza os dados de uma transação existente com base no seu ID
    @Override
    public void update(Transaction transaction) throws SQLException {

        String sql = """
                     UPDATE transactions
                     SET name = ?, value = ?, type = ?, date = ?, description = ?
                     WHERE id = ?      
                     """;
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, transaction.getName());
            stmt.setBigDecimal(2, transaction.getValue());
            stmt.setString(3, transaction.getType());
            stmt.setString(4, transaction.getDate() != null ? transaction.getDate().toString() : null);
            stmt.setString(5, transaction.getDescription());
            stmt.setInt(6, transaction.getId());
            
            stmt.executeUpdate();
        }
    }

    // Remove um registro da tabela transactions utilizando o ID fornecido
    @Override
    public void remove(int id) throws SQLException {

        String sql = "DELETE FROM transactions WHERE id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                
            stmt.setInt(1, id);

            stmt.executeUpdate();
        }
    }
}