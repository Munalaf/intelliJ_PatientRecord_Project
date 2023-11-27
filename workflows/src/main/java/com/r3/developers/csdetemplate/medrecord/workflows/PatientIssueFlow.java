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
import net.corda.v5.ledger.common.NotaryLookup;
import net.corda.v5.ledger.utxo.UtxoLedgerService;
import net.corda.v5.ledger.utxo.transaction.UtxoSignedTransaction;
import net.corda.v5.ledger.utxo.transaction.UtxoTransactionBuilder;
import net.corda.v5.membership.MemberInfo;
import net.corda.v5.membership.NotaryInfo;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import static java.util.Objects.requireNonNull;

public class PatientIssueFlow implements ClientStartableFlow {

    @CordaInject
    public JsonMarshallingService jsonMarshallingService;

    // Injects the MemberLookup to look up the VNode identities.
    @CordaInject
    public MemberLookup memberLookup;

    // Injects the UtxoLedgerService to enable the flow to make use of the Ledger API.
    @CordaInject
    public UtxoLedgerService ledgerService;

    // Injects the NotaryLookup to look up the notary identity.
    @CordaInject
    public NotaryLookup notaryLookup;

    @CordaInject
    public FlowEngine flowEngine;

    @NotNull
    @Override
    @Suspendable
    public String call(@NotNull ClientRequestBody requestBody) {

        try {
            // Obtain the deserialized input arguments to the flow from the requestBody.
            PatientIssueFlowArgs flowArgs = requestBody.getRequestBodyAs(jsonMarshallingService, PatientIssueFlowArgs.class);

            // Get MemberInfos for the Vnode running the flow and the otherMember.
            MemberInfo patientInfo = memberLookup.myInfo(); //Here should really be patient info
            MemberInfo hospitalInfo = requireNonNull(
                    memberLookup.lookup(MemberX500Name.parse(flowArgs.getCurrentHospital())),
                    "MemberLookup can't find otherMember specified in flow arguments."
            );

            // Create the Patient State from the input arguments and member information.
            PatientState patientState = new PatientState(
                    patientInfo.getName(),
                    flowArgs.getAge(),
                    flowArgs.getAddress(),
                    flowArgs.getContactNumber(),
                    flowArgs.getDiagnosis(),
                    flowArgs.getWeight(),
                    flowArgs.getHeight(),
                    flowArgs.getBloodType(),
                    flowArgs.getMediciations(),
                    flowArgs.getLatestDiagnosis(),
                    flowArgs.getId(),
                    flowArgs.getRecord(),
                    flowArgs.getListOfSurgereies(),
                    hospitalInfo.getName(),
                    Arrays.asList(patientInfo.getLedgerKeys().get(0), hospitalInfo.getLedgerKeys().get(0)) // to put patient and hopsital as participents
            );

            // Obtain the Notary name and public key.
            NotaryInfo notary = requireNonNull(
                    notaryLookup.lookup(MemberX500Name.parse("CN=NotaryService, OU=Test Dept, O=R3, L=London, C=GB")),
                    "NotaryLookup can't find notary specified in flow arguments."
            );

            // Use UTXOTransactionBuilder to build up the draft transaction.
            UtxoTransactionBuilder txBuilder = ledgerService.createTransactionBuilder()
                    .setNotary(notary.getName())
                    .setTimeWindowBetween(Instant.now(), Instant.now().plusMillis(Duration.ofDays(1).toMillis()))
                    .addOutputState(patientState)
                    .addCommand(new PatientContract.Issue())
                    .addSignatories(patientState.getParticipants());

            UtxoSignedTransaction signedTransaction = txBuilder.toSignedTransaction(); // self sign transaction


            return flowEngine.subFlow(new FinalizePatientFlow.FinalizePatient(signedTransaction, Arrays.asList(hospitalInfo.getName())));


        }
        // Catch any exceptions, log them and rethrow the exception.
        catch (Exception e) {
            throw new CordaRuntimeException(e.getMessage());
        }

    }
}


/*
{
  "clientRequestId": "issue1",
  "flowClassName": "com.r3.developers.csdetemplate.medrecord.workflows.PatientIssueFlow",
  "requestBody": {
      "name": "CN=Bob, OU=Test Dept, O=R3, L=London, C=GB",
 "age":"85",
 "address": "Abudhabi",
 "contactNumber": "0551234567",
 "diagnosis": "cancer",
 "weight": "67",
 "height": "160",
 "bloodType": "A+",
 "mediciations": "Brofun",
 "latestDiagnosis":["bone surgery"],
 "id": "7842999",
 "record": [["hello world"]],
 "listOfSurgereies": ["bone surgery","brain surgery"],
 "currentHospital": "CN=Alice, OU=Test Dept, O=R3, L=London, C=GB"
  }
}
*/