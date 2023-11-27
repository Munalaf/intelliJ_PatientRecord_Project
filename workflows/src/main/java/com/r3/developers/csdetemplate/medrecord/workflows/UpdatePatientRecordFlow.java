package com.r3.developers.csdetemplate.medrecord.workflows;

import com.r3.developers.csdetemplate.utxoexample.contracts.PatientContract;
import com.r3.developers.csdetemplate.utxoexample.states.PatientState;
import net.corda.v5.application.flows.ClientRequestBody;
import net.corda.v5.application.flows.ClientStartableFlow;
import net.corda.v5.application.flows.CordaInject;
import net.corda.v5.application.flows.FlowEngine;
import net.corda.v5.application.marshalling.JsonMarshallingService;
import net.corda.v5.application.membership.MemberLookup;
import net.corda.v5.base.annotations.Suspendable;
import net.corda.v5.base.exceptions.CordaRuntimeException;
import net.corda.v5.base.types.MemberX500Name;
import net.corda.v5.ledger.utxo.StateAndRef;
import net.corda.v5.ledger.utxo.UtxoLedgerService;
import net.corda.v5.ledger.utxo.transaction.UtxoSignedTransaction;
import net.corda.v5.ledger.utxo.transaction.UtxoTransactionBuilder;
import net.corda.v5.membership.MemberInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

// See patient CorDapp Design section of the getting started docs for a description of this flow.
    public class UpdatePatientRecordFlow implements ClientStartableFlow {

        private final static Logger log = LoggerFactory.getLogger(UpdatePatientRecordFlow.class);

        @CordaInject
        public JsonMarshallingService jsonMarshallingService;

        @CordaInject
        public MemberLookup memberLookup;

        // Injects the UtxoLedgerService to enable the flow to make use of the Ledger API.
        @CordaInject
        public UtxoLedgerService ledgerService;

        // FlowEngine service is required to run SubFlows.
        @CordaInject
        public FlowEngine flowEngine;

        @Suspendable
        @Override

        public String call(ClientRequestBody requestBody) {

            log.info("UpdateNewpatientFlow.call() called");

            try {
                // Obtain the deserialized input arguments to the flow from the requestBody.
                UpdatePatientRecordFlowArgs flowArgs = requestBody.getRequestBodyAs(jsonMarshallingService, UpdatePatientRecordFlowArgs.class);

                // Look up the latest unconsumed patientState with the given id.
                // Note, this code brings all unconsumed states back, then filters them.
                // This is an inefficient way to perform this operation when there are a large number of patients.
                // Note, you will get this error if you input an id which has no corresponding patientState (common error).
                List<StateAndRef<PatientState>> patientStateAndRefs = ledgerService.findUnconsumedStatesByType(PatientState.class);
                List<StateAndRef<PatientState>> patientStateAndRefsWithId = patientStateAndRefs.stream()
                        .filter(sar -> sar.getState().getContractState().getId().equals(flowArgs.getId())).collect(toList());
                if (patientStateAndRefsWithId.size() != 1) throw new CordaRuntimeException("Multiple or zero patient states with id " + flowArgs.getId() + " found");
                StateAndRef<PatientState> patientStateAndRef = patientStateAndRefsWithId.get(0);

                // Get MemberInfos for the Vnode running the flow and the otherMember.
                MemberInfo myInfo = memberLookup.myInfo();
                PatientState state = patientStateAndRef.getState().getContractState();



                // Create a new patientState using the updateMessage helper function.
                PatientState newpatientState = state.updateDiagnosisAndRecord(flowArgs.getDiagnosis(),flowArgs.getRecord());
                MemberX500Name patientName = newpatientState.getName();



                // Use UTXOTransactionBuilder to build up the draft transaction.
                UtxoTransactionBuilder txBuilder = ledgerService.createTransactionBuilder()
                        .setNotary(patientStateAndRef.getState().getNotaryName())
                        .setTimeWindowBetween(Instant.now(), Instant.now().plusMillis(Duration.ofDays(1).toMillis()))
                        .addOutputState(newpatientState)
                        .addInputState(patientStateAndRef.getRef())
                        .addCommand(new PatientContract.Transfer())
                        .addSignatories(newpatientState.getParticipants());

                // Convert the transaction builder to a UTXOSignedTransaction. Verifies the content of the
                // UtxoTransactionBuilder and signs the transaction with any required signatories that belong to
                // the current node.
                UtxoSignedTransaction signedTransaction = txBuilder.toSignedTransaction();
                // Call FinalizepatientSubFlow which will finalise the transaction.
                // If successful the flow will return a String of the created transaction id,
                // if not successful it will return an error message.
                return flowEngine.subFlow(new FinalizePatientFlow.FinalizePatient(signedTransaction, Arrays.asList(patientName)));


            }
            // Catch any exceptions, log them and rethrow the exception.
            catch (Exception e) {
                log.warn("Failed to process utxo flow for request body " + requestBody + " because: " + e.getMessage());
                throw e;
            }
        }
}

/*
RequestBody for triggering the flow via REST:
{
    "clientRequestId": "update-1",
    "flowClassName": "com.r3.developers.csdetemplate.utxoexample.workflows.UpdatePatientRecordFlow",
    "requestBody": {
        "id":" ** fill in id **",
        "diagnosis": "How are you today?",
        "record": ["HA Record"]
        }
}
 */

