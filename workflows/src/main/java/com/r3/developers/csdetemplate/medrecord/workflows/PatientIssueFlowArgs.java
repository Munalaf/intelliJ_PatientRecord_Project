package com.r3.developers.csdetemplate.medrecord.workflows;

import java.util.List;

public class PatientIssueFlowArgs {

    private String name;
    private int age;
    private String address;
    private String contactNumber;
    private String diagnosis;
    private int weight;
    private int height;
    private String bloodType;
    private String mediciations;
    private List<String> latestDiagnosis;
    private String id ;
    private List<List<String>> record;
    private List<String> listOfSurgereies;
    private String currentHospital;

    public PatientIssueFlowArgs() {
    }

    public PatientIssueFlowArgs(String name, int age, String address, String contactNumber, String diagnosis, int weight, int height, String bloodType, String mediciations, List<String> latestDiagnosis, String id, List<List<String>> record, List<String> listOfSurgereies, String currentHospital) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.contactNumber = contactNumber;
        this.diagnosis = diagnosis;
        this.weight = weight;
        this.height = height;
        this.bloodType = bloodType;
        this.mediciations = mediciations;
        this.latestDiagnosis = latestDiagnosis;
        this.id = id;
        this.record = record;
        this.listOfSurgereies = listOfSurgereies;
        this.currentHospital = currentHospital;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public int getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getMediciations() {
        return mediciations;
    }

    public List<String> getLatestDiagnosis() {
        return latestDiagnosis;
    }

    public String getId() {
        return id;
    }

    public List<List<String>> getRecord() {
        return record;
    }

    public List<String> getListOfSurgereies() {
        return listOfSurgereies;
    }

    public String getCurrentHospital() {
        return currentHospital;
    }
}
