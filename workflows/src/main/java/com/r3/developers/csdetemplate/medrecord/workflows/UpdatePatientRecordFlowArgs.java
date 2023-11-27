package com.r3.developers.csdetemplate.medrecord.workflows;

import net.corda.v5.base.annotations.ConstructorForDeserialization;
import net.corda.v5.base.types.MemberX500Name;
import org.jetbrains.annotations.NotNull;

import java.security.PublicKey;
import java.util.List;

public class UpdatePatientRecordFlowArgs {
  
    private String id; 
    private String diagnosis;
  
    private List<String> record;

    public UpdatePatientRecordFlowArgs() {
    }

    public UpdatePatientRecordFlowArgs(String id, String diagnosis, List<String> record) {
        this.id = id;
        this.diagnosis = diagnosis;
        this.record = record;
    }

    public String getId() {
        return id;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public List<String> getRecord() {
        return record;
    }
    
}


