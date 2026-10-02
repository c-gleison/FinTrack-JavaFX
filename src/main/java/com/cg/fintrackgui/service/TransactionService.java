package com.cg.fintrackgui.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import com.cg.fintrackgui.dao.TransactionDAO;
import com.cg.fintrackgui.model.Transaction;
import com.cg.fintrackgui.repository.GenericRepository;
import com.cg.fintrackgui.util.ValidationUtils;

// Serviço que coordena o DAO (banco) e o repositório genérico (memória)
public class TransactionService {

    // Acesso à persistência no banco SQLite
    private final TransactionDAO dao = new TransactionDAO();
    // Cache em memória das transações já carregadas do banco
    private final GenericRepository<Transaction> repository = new GenericRepository<>();

    // Recarrega o cache em memória a partir do banco
    public void reload() throws SQLException {
        repository.clear();
        repository.addAll(dao.findAll());
    }

    // Copia as transações em cache para uma lista de destino
    public void copyTo(List<? super Transaction> destination) {
        repository.copyTo(destination);
    }

    // Persiste e depois sincroniza o cache
    public void add(Transaction t) throws SQLException {
        dao.save(t);
        reload();
    }

    // Atualiza a transação no banco e sincroniza o cache
    public void update(Transaction t) throws SQLException {
        dao.update(t);
        reload();
    }

    // Remove a transação do banco pelo ID e sincroniza o cache
    public void remove(String id) throws SQLException {
        dao.remove(id);
        reload();
    }

    // Calcula o saldo a partir das transações em cache 
    public BigDecimal getBalance() {
        return ValidationUtils.calculateTotalBalance(repository.findAll());
    }

}