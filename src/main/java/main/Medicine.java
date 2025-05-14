/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.sql.Date;

public class Medicine {
    private int medicineId;
    private String name;
    private String brand;
    private String description;
    private int quantityStock;
    private Date expiryDate;

    // Constructor
    public Medicine(int medicineId, String name, String description, int quantityStock, Date expiryDate, String brand) {
        this.medicineId = medicineId;
        this.name = name;
        this.description = description;
        this.quantityStock = quantityStock;
        this.expiryDate = expiryDate;
        this.brand = brand;
    }

    // Getters and Setters
    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getBrand() {
        return brand;
    }

    public void setBrand(String name) {
        this.brand = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantityStock() {
        return quantityStock;
    }

    public void setQuantityStock(int quantityStock) {
        this.quantityStock = quantityStock;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
