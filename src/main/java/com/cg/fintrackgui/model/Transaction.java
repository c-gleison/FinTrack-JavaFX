package com.cg.fintrackgui.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {

    String name;
    String description;
    BigDecimal value;
    LocalDate date;

    public Transaction(String name, String description, BigDecimal value, LocalDate date) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.date = date;
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
    
}
