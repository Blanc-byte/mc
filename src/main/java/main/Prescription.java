/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;
public class Prescription {
    private int prescriptionId;
    private String patientName;
    private String consultationType;
    private String consultationDate;
    private String diagnosis;
    private String dosage;
    private String frequency;
    private String duration;

    public Prescription(int prescriptionId, String patientName, String consultationType, String consultationDate, String diagnosis, String dosage, String frequency, String duration) {
        this.prescriptionId = prescriptionId;
        this.patientName = patientName;
        this.consultationType = consultationType;
        this.consultationDate = consultationDate;
        this.diagnosis = diagnosis;
        this.dosage = dosage;
        this.frequency = frequency;
        this.duration = duration;
    }

    // Getters and setters
    public int getPrescriptionId() {
        return prescriptionId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getConsultationType() {
        return consultationType;
    }

    public String getConsultationDate() {
        return consultationDate;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getDosage() {
        return dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getDuration() {
        return duration;
    }
}
