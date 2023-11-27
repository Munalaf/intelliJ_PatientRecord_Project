package com.r3.developers.csdetemplate.medrecord.workflows;

public class TransferRecordFlowArgs {

    private String id;


    private String oldHospital;

    public TransferRecordFlowArgs() {
    }

    public TransferRecordFlowArgs(String id, String oldHospital) {
        this.id = id;
        this.oldHospital = oldHospital;
    }

    public String getId() {
        return id;
    }

    public String getOldHospital() {
        return oldHospital;
    }
}
