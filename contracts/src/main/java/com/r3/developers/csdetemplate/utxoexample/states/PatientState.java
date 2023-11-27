package com.r3.developers.csdetemplate.utxoexample.states;

import com.r3.developers.csdetemplate.utxoexample.contracts.PatientContract;
import net.corda.v5.base.annotations.ConstructorForDeserialization;
import net.corda.v5.base.types.MemberX500Name;
import net.corda.v5.ledger.utxo.BelongsToContract;
import net.corda.v5.ledger.utxo.ContractState;
import org.jetbrains.annotations.NotNull;

import java.security.PublicKey;
import java.util.List;

@BelongsToContract(PatientContract.class)
public class PatientState implements ContractState {

    private final MemberX500Name name;
    private final int age;
    private final String address;
    private final String contactNumber;
    private final String diagnosis;
    private final int weight;
    private final int height;
    private final String bloodType;
    private final String mediciations;
    private final List<String> latestDiagnosis;
    private final String id ;
    private final List<List<String>> record;
    private final List<String> listOfSurgereies;
    private final MemberX500Name currentHospital;
    private List<PublicKey> participants;

    //Old state name,diagnosis ,record ,age // Now state // as a programer i need to send the old state to new state
    //
    @ConstructorForDeserialization

    public PatientState(MemberX500Name name, int age, String address, String contactNumber, String diagnosis, int weight, int height, String bloodType, String mediciations, List<String> latestDiagnosis, String id, List<List<String>> record, List<String> listOfSurgereies, MemberX500Name currentHospital, List<PublicKey> participants) {
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
        this.participants = participants;
    }

    public MemberX500Name getName() {
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

    public MemberX500Name getCurrentHospital() {
        return currentHospital;
    }

    @NotNull
    @Override
    public List<PublicKey> getParticipants() {
        return participants;    }

    public PatientState updateDiagnosisAndRecord(String diagnosis, List<String> record) {
        this.record.add(record);
        return new PatientState(name,age,address,contactNumber,diagnosis,weight,height,bloodType,mediciations,latestDiagnosis,id, this.record,listOfSurgereies,currentHospital,participants);
    }
}
