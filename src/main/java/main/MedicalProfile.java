/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;
public class MedicalProfile {
    private int profileId;
    private String patientName;
    private String bloodType;
    private String allergies;
    private String preExistingConditions;
    private String vaccinations;

    // Constructor
    public MedicalProfile(int profileId, String patientName, String bloodType, String allergies, 
                          String preExistingConditions, String vaccinations) {
        this.profileId = profileId;
        this.patientName = patientName;
        this.bloodType = bloodType;
        this.allergies = allergies;
        this.preExistingConditions = preExistingConditions;
        this.vaccinations = vaccinations;
    }

    // Getters
    public int getProfileId() {
        return profileId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getAllergies() {
        return allergies;
    }

    public String getPreExistingConditions() {
        return preExistingConditions;
    }

    public String getVaccinations() {
        return vaccinations;
    }
}
