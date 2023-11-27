package com.r3.developers.csdetemplate.medrecord.workflows;

import com.r3.developers.csdetemplate.utxoexample.states.ChatState;
import com.r3.developers.csdetemplate.utxoexample.states.PatientState;
import com.r3.developers.csdetemplate.utxoexample.workflows.ChatStateResults;
import com.r3.developers.csdetemplate.utxoexample.workflows.ListChatsFlow;
import net.corda.v5.application.flows.ClientRequestBody;
import net.corda.v5.application.flows.ClientStartableFlow;
import net.corda.v5.application.flows.CordaInject;
import net.corda.v5.application.marshalling.JsonMarshallingService;
import net.corda.v5.base.annotations.Suspendable;
import net.corda.v5.ledger.utxo.StateAndRef;
import net.corda.v5.ledger.utxo.UtxoLedgerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class ListPatientRecordFlow implements ClientStartableFlow {
    private final static Logger log = LoggerFactory.getLogger(ListPatientRecordFlow.class);

    @CordaInject
    public JsonMarshallingService jsonMarshallingService;

    // Injects the UtxoLedgerService to enable the flow to make use of the Ledger API.
    @CordaInject
    public UtxoLedgerService utxoLedgerService;

    @Suspendable
    @Override
    public String call(ClientRequestBody requestBody) {

        log.info("ListPatientRecordFlow.call() called");

        // Queries the VNode's vault for unconsumed states and converts the result to a serializable DTO.
        List<StateAndRef<PatientState>> states = utxoLedgerService.findUnconsumedStatesByType(PatientState.class);
        List<PatientRecordResults> results = states.stream().map(stateAndRef ->
                new PatientRecordResults(
                        stateAndRef.getState().getContractState().getName().getCommonName(),
                        stateAndRef.getState().getContractState().getAge(),
                        stateAndRef.getState().getContractState().getAddress(),
                        stateAndRef.getState().getContractState().getContactNumber(),
                        stateAndRef.getState().getContractState().getDiagnosis(),
                        stateAndRef.getState().getContractState().getWeight(),
                        stateAndRef.getState().getContractState().getHeight(),
                        stateAndRef.getState().getContractState().getBloodType(),
                        stateAndRef.getState().getContractState().getMediciations(),
                        stateAndRef.getState().getContractState().getLatestDiagnosis(),
                        stateAndRef.getState().getContractState().getId(),
                        stateAndRef.getState().getContractState().getRecord(),
                        stateAndRef.getState().getContractState().getListOfSurgereies(),
                        stateAndRef.getState().getContractState().getCurrentHospital().getCommonName()
                )
        ).collect(Collectors.toList());

        // Uses the JsonMarshallingService's format() function to serialize the DTO to Json.
        return jsonMarshallingService.format(results);
    }
}