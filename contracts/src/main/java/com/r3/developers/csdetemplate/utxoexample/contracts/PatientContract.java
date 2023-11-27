package com.r3.developers.csdetemplate.utxoexample.contracts;

import com.r3.developers.csdetemplate.utxoexample.states.PatientState;
import net.corda.v5.base.exceptions.CordaRuntimeException;
import net.corda.v5.ledger.utxo.Command;
import net.corda.v5.ledger.utxo.Contract;
import net.corda.v5.ledger.utxo.transaction.UtxoLedgerTransaction;
import org.jetbrains.annotations.NotNull;

public class PatientContract implements Contract {

    public static class Issue implements Command {}
    public static class  Transfer implements Command{}
    @Override
    public void verify(@NotNull UtxoLedgerTransaction transaction) {

        // Ensures that there is only one command in the transaction
        requireThat( transaction.getCommands().size() == 1, "Require a single command.");
        Command command = transaction.getCommands().get(0);
        PatientState output = transaction.getOutputStates(PatientState.class).get(0);
        requireThat(output.getParticipants().size() == 2, "The output state should have two and only two participants.");

        // Switches case based on the command
        if(command.getClass() == PatientContract.Issue.class) {// Rules applied only to transactions with the Issue Command.
            requireThat(transaction.getOutputContractStates().size() == 1, "Only one output states should be created when issuing an IOU.");
        }else if(command.getClass() == PatientContract.Transfer.class) {// Rules applied only to transactions with the Transfer Command.
            requireThat( transaction.getInputContractStates().size() > 0, "There must be one input IOU.");
        }

        else {
            throw new CordaRuntimeException("Unsupported command");
        }
    }

    // Helper function to allow writing constraints in the Corda 4 '"text" using (boolean)' style
    private void requireThat(boolean asserted, String errorMessage) {
        if(!asserted) {
            throw new CordaRuntimeException("Failed requirement: " + errorMessage);
        }
    }
}
