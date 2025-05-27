package org.example.topkapihazinensi.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Expense {

    private final SimpleStringProperty description;
    private final SimpleDoubleProperty amount;
    private final SimpleStringProperty date;
    private int category_id;
    private final SimpleStringProperty userId;

    public Expense(String description, double amount, int category_id, String date, String userId) {
        this.description = new SimpleStringProperty(description);
        this.amount = new SimpleDoubleProperty(amount);
        this.category_id = category_id;
        this.date = new SimpleStringProperty(date);
        this.userId = new SimpleStringProperty(userId);
    }

    public String getDescription() { return description.get(); }
    public void setDescription(String value) { description.set(value); }

    public double getAmount() { return amount.get(); }
    public void setAmount(double value) { amount.set(value); }

    public String getDate() { return date.get(); }
    public void setDate(String value) { date.set(value); }

    public int getCategory() {
        return category_id;
    }
    public void setCategory(int value) {
        this.category_id = value;
    }

    public String getUserId() { return userId.get(); }
    public void setUserId(String value) { userId.set(value); }
}
