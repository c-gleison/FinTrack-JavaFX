package com.cg.fintrackgui.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {

    private int id;
    private String name;
    private BigDecimal value;
    private String type;
    private LocalDate date;
    private String description;

    public Transaction(int id, String name, BigDecimal value, String type, LocalDate date, String description) {
        
        this.id = id;
        this.name = name;
        this.value = value;
        this.date = date;
        this.type = type;
        this.description = description;

    }

    // Getters
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public BigDecimal getValue() {
        return value;
    }
    public LocalDate getDate() {
        return date;
    }
    public String getType(){
        return type;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setValue(BigDecimal value) {
        this.value = value;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }  
    public void setType(String type){
        this.type = type;
    }
    
}
