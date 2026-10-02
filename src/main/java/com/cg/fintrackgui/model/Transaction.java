package com.cg.fintrackgui.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

// Modelo que representa uma transação financeira no sistema
public class Transaction {

    // Atributos da transação financeira
    private String id;
    private String name;
    private BigDecimal value;
    private String type;
    private LocalDate date;
    private String description;
    private Month recurrencyMonth;

    // Construtor completo para inicialização da transação
    public Transaction(String id, String name, BigDecimal value, String type, LocalDate date, Month recurrencyMonth, String description) {
        
        this.id = id;
        this.name = name;
        this.value = value;
        this.date = date;
        this.type = type;
        this.recurrencyMonth = recurrencyMonth;
        this.description = description;
    }

    // Métodos de leitura dos dados da transação
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public BigDecimal getValue() {
        return value;
    }
    public String getType(){
        return type;
    }
    public LocalDate getDate() {
        return date;
    }
    public Month getRecurrencyMonth(){
        return recurrencyMonth;
    }

    public String getDescription() {
        return description;
    }
    

    // Métodos de alteração dos dados da transação
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setValue(BigDecimal value) {
        this.value = value;
    }
    public void setType(String type){
        this.type = type;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }  
    public void setRecurrencyMonth(Month recurrencyMonth){
        this.recurrencyMonth = recurrencyMonth;
    } 

    public void setDescription(String description) {
        this.description = description;
    }

}