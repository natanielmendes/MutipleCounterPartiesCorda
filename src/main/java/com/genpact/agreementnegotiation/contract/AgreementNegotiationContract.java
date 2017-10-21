package com.genpact.agreementnegotiation.contract;

import com.genpact.agreementnegotiation.state.AgreementNegotiationState;
import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.Contract;
import net.corda.core.identity.Party;
import net.corda.core.transactions.LedgerTransaction;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;
import static net.corda.core.contracts.ContractsDSL.requireThat;
/**
 * Define your contract here.
 */
public class AgreementNegotiationContract implements Contract {
    // This is used to identify our contract when building a transaction.
    public static final String TEMPLATE_CONTRACT_ID = "AgreementNegotiationContract";



    public interface Commands extends CommandData {
        class Initiate implements Commands {
            @Override
            public boolean equals(Object obj) {
                return obj instanceof Initiate;
            }
        }

        class Amend implements Commands {
            @Override
            public boolean equals(Object obj) {
                return obj instanceof Amend;
            }
        }
    }

    // Our Create commands.
    public static class Initiate implements CommandData {}
    public static class Amend implements CommandData {}
    public static class Accept implements CommandData {}
    /**
     * A transaction is considered valid if the verify() function of the contract of each of the transaction's input
     * and output states does not throw an exception.
     */
    @Override
    public void verify(LedgerTransaction tx) {

        final AgreementNegotiationState out = (AgreementNegotiationState) tx.getOutputs().get(0).getData();
        final Party cptyA = out.getCptyInitiator();
        final Party cptyB = out.getCptyReciever();

        requireThat(check -> {
            // IOU-specific constraints.
            if(out.getNegotiationState() == AgreementNegotiationState.NegotiationStates.INITIAL) {
                final CommandWithParties<Initiate> command = requireSingleCommand(tx.getCommands(), Initiate.class);
                // Constraints on the shape of the transaction.
                check.using("No inputs should be consumed when issuing an IOU.", tx.getInputs().isEmpty());
                check.using("There should be one output state of type AgreementNegotiationState.", tx.getOutputs().size() == 1);


                //   check.using("The Agreement Parameters's value must be Initialized.",out.getValue().isInitialized()==true);
                check.using("The Initiator and the Reciever cannot be the same entity.", cptyA != cptyB);

                // Constraints on the signers.
                check.using("There must only be two signer.", command.getSigners().size() == 2);
                check.using("The signer must be the cptyA.", command.getSigners().containsAll(
                        ImmutableList.of(cptyA.getOwningKey(), cptyB.getOwningKey())));
            }
            else if(out.getNegotiationState() == AgreementNegotiationState.NegotiationStates.AMEND)
            {
                final CommandWithParties<Amend> command = requireSingleCommand(tx.getCommands(), Amend.class);
                check.using("Inputs should be empty state.", !tx.getInputs().isEmpty());
                check.using("There should be one output state of type AgreementNegotiationState.", tx.getOutputs().size() == 1);


                //   check.using("The Agreement Parameters's value must be Initialized.",out.getValue().isInitialized()==true);
                check.using("The Initiator and the Reciever cannot be the same entity.", cptyA != cptyB);

                // Constraints on the signers.
                check.using("There must only be two signer.", command.getSigners().size() == 2);
            }
            else if(out.getNegotiationState() == AgreementNegotiationState.NegotiationStates.ACCEPT)
            {
                final CommandWithParties<Accept> command = requireSingleCommand(tx.getCommands(), Accept.class);
                check.using("Inputs should be empty state.", !tx.getInputs().isEmpty());
                check.using("There should be one output state of type AgreementNegotiationState.", tx.getOutputs().size() == 1);


                //   check.using("The Agreement Parameters's value must be Initialized.",out.getValue().isInitialized()==true);
                check.using("The Initiator and the Reciever cannot be the same entity.", cptyA != cptyB);

                // Constraints on the signers.
                check.using("There must only be two signer.", command.getSigners().size() == 2);
            }
            return null;
        });
    }
}