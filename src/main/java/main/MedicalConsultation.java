/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

public class MedicalConsultation {
    private int consultationId;
    private String patientName; // Combine first_name and last_name
    private String consultationDate;
    private String consultationType;
    private String diagnosis;
    private String prescribedTreatment;

    public MedicalConsultation(int consultationId, String patientName, String consultationDate,
                               String consultationType, String diagnosis, String prescribedTreatment) {
        this.consultationId = consultationId;
        this.patientName = patientName;
        this.consultationDate = consultationDate;
        this.consultationType = consultationType;
        this.diagnosis = diagnosis;
        this.prescribedTreatment = prescribedTreatment;
    }

    public int getConsultationId() {
        return consultationId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getConsultationDate() {
        return consultationDate;
    }

    public String getConsultationType() {
        return consultationType;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getPrescribedTreatment() {
        return prescribedTreatment;
    }
}
