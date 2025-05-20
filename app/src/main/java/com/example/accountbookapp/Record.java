package com.example.accountbookapp;

import java.io.Serializable;
import java.util.Date;

public class Record implements Serializable {
    private int id;
    private double amount;
    private String type; // "income" 或 "expense"
    private String category;
    private String note;
    private Date date;

    public Record() {}

    public Record(double amount, String type, String category, String note, Date date) {
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.note = note;
        this.date = date;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}  