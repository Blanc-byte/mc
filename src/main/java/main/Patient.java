/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.time.LocalDate;
public class Patient {
    private int id; // Added id field
    private String patientId;
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate birthDate;
    private int age;
    private String category;
    private String contactInfo;
    private String medicalHistory;
    private String healthStatus;

    // Constructor
    public Patient(int id, String patientId, String firstName, String lastName, String gender, LocalDate birthDate, int age, String category, String contactInfo, String medicalHistory, String healthStatus) {
        this.id = id;
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.age = age;
        this.category = category;
        this.contactInfo = contactInfo;
        this.medicalHistory = medicalHistory;
        this.healthStatus = healthStatus;
    }

    // Getters and setters
    public int getId() { return id; }  // Added getter for id
    public String getPatientId() { return patientId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getGender() { return gender; }
    public LocalDate getBirthDate() { return birthDate; }
    public int getAge() { return age; }
    public String getCategory() { return category; }
    public String getContactInfo() { return contactInfo; }
    public String getMedicalHistory() { return medicalHistory; }
    public String getHealthStatus() { return healthStatus; }
}
