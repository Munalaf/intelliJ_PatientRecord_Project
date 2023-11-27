package com.r3.developers.csdetemplate.medrecord.workflows;

import com.r3.developers.csdetemplate.utxoexample.states.PatientState;
import net.corda.v5.application.flows.*;
import net.corda.v5.application.messaging.FlowMessaging;
import net.corda.v5.application.messaging.FlowSession;
import net.corda.v5.base.annotations.Suspendable;
import net.corda.v5.base.exceptions.CordaRuntimeException;
import net.corda.v5.base.types.MemberX500Name;
import net.corda.v5.ledger.utxo.UtxoLedgerService;
import net.corda.v5.ledger.utxo.transaction.UtxoSignedTransaction;
import net.corda.v5.ledger.utxo.transaction.UtxoTransactionValidator;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TransferRecordFlow  {
    private final static Logger log = LoggerFactory.getLogger(com.r3.developers.csdetemplate.medrecord.workflows.FinalizePatientFlow.class);


    @InitiatingFlow(protocol = "finalize-transfer-protocol")
    public static class TransferRecord implements ClientStartableFlow {

        // Injects the UtxoLedgerService to enable the flow to make use of the Ledger API.
        @CordaInject
        public UtxoLedgerService ledgerService;

        @CordaInject
        public FlowMessaging flowMessaging;


        @NotNull
        @Override
        @Suspendable
        public String call(@NotNull ClientRequestBody requestBody) {
            //NOTE: the caller of this method is the hospital

            //get the info from flow args.
                //id of the patient record
                // who is the old hospital.

            //you will initiate a communication channel. with the flow called initiateFlow()


            //you will send the ID to the old hoipital via the method called SendAll()


            //now technically, the opearation will switch to the Responder (which is hosipital A) the code will be below in the responder.


            //-----------------
            //now the initiator recieves the transaction and sign it.
            //And the initiator part is done.


            return null;
    }
    }

    @InitiatedBy(protocol = "finalize-transfer-protocol")
    public static class TransferRecordResponderFlow implements ResponderFlow {

        // Injects the UtxoLedgerService to enable the flow to make use of the Ledger API.
        @CordaInject
        public UtxoLedgerService utxoLedgerService;

        @Suspendable
        @Override
        public void call(FlowSession session) {

            log.info("FinalizePatientResponderFlow.call() called");



            try {

                //you recieve the id from the newHosipital. and you do a query in the database (Just like you did in the updatePatientRecord flow)

                //Then, you start buidiling the transaction.

                //you self sign.

                //you give to transaction back to newHosipital and the Patient to sign.

                //now the flow jump back to the initiator.


                //----------the responder gets the transaction fully signed, and call finality flow.
                //flow ends.




            }
            // Soft fails the flow and log the exception.
            catch(Exception e)
            {
                log.warn("Exceptionally finished responder flow", e);
            }




        }
    }




}
