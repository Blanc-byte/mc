/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GiveMed {
    private final IntegerProperty giveMedId;
    private final StringProperty patientName;
    private final StringProperty medicineName;
    private final IntegerProperty quantity;
    private final StringProperty giveDate;

    public GiveMed(int giveMedId, String patientName, String medicineName, int quantity, String giveDate) {
        this.giveMedId = new SimpleIntegerProperty(giveMedId);
        this.patientName = new SimpleStringProperty(patientName);
        this.medicineName = new SimpleStringProperty(medicineName);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.giveDate = new SimpleStringProperty(giveDate);
    }

    public int getGiveMedId() {
        return giveMedId.get();
    }

    public String getPatientName() {
        return patientName.get();
    }

    public String getMedicineName() {
        return medicineName.get();
    }

    public int getQuantity() {
        return quantity.get();
    }

    public String getGiveDate() {
        return giveDate.get();
    }
}
