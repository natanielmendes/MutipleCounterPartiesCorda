package com.genpact.agreementnegotiation;

import com.google.common.collect.ImmutableList;
import net.corda.core.concurrent.CordaFuture;
import net.corda.core.identity.CordaX500Name;
import net.corda.node.services.transactions.ValidatingNotaryService;
import net.corda.nodeapi.User;
import net.corda.nodeapi.internal.ServiceInfo;
import net.corda.testing.driver.DriverParameters;
import net.corda.testing.driver.NodeHandle;
import net.corda.testing.driver.NodeParameters;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.singleton;
import static net.corda.testing.driver.Driver.driver;

/**
 * This file is exclusively for being able to run your nodes through an IDE (as opposed to using deployNodes)
 * Do not use in a production environment.
 * <p>
 * To debug your CorDapp:
 * <p>
 * 1. Firstly, run the "Run Template CorDapp" run configuration.
 * 2. Wait for all the nodes to start.
 * 3. Note the debug ports which should be output to the console for each node. They typically start at 5006, 5007,
 * 5008. The "Debug CorDapp" configuration runs with port 5007, which should be "NodeB". In any case, double check
 * the console output to be sure.
 * 4. Set your breakpoints in your CorDapp code.
 * 5. Run the "Debug CorDapp" remote debug run configuration.
 */
public class Main {
    public static void main(String[] args) {
        // No permissions required as we are not invoking flows.
        Set<String> permissions = new HashSet<String>();
        permissions.add("StartFlow.com.genpact.agreementnegotiation.flow.AgreementNegotiationInitiateFlow$Initiator");
        permissions.add("StartFlow.com.genpact.agreementnegotiation.flow.AgreementNegotiationAmendFlow$Initiator");
        permissions.add("StartFlow.com.genpact.agreementnegotiation.flow.AgreementNegotiationAcceptFlow$Initiator");
        final User user = new User("user1", "test", permissions );
        driver(new DriverParameters().setIsDebug(true), dsl -> {
                    dsl.startNode(new NodeParameters()
                            .setProvidedName(new CordaX500Name("Controller", "London", "GB"))
                            .setAdvertisedServices(singleton(new ServiceInfo(ValidatingNotaryService.Companion.getType(), null))));

                    CordaFuture<NodeHandle> nodeAFuture = dsl.startNode(new NodeParameters()
                            .setProvidedName(new CordaX500Name("JPMorgan Chase", "London", "GB"))
                            .setRpcUsers(ImmutableList.of(user)));
                    CordaFuture<NodeHandle> nodeB = dsl.startNode(new NodeParameters()
                            .setProvidedName(new CordaX500Name("Bank of America", "New York", "US"))
                            .setRpcUsers(ImmutableList.of(user)));
    /*        CordaFuture<NodeHandle> nodeC = dsl.startNode(new NodeParameters()
                    .setProvidedName(new CordaX500Name("Genpact", "New Delhi", "IN"))
                    .setRpcUsers(ImmutableList.of(user)));
            CordaFuture<NodeHandle> nodeD = dsl.startNode(new NodeParameters()
                    .setProvidedName(new CordaX500Name("Macquarie", "Sydney", "AU"))
                    .setRpcUsers(ImmutableList.of(user)));
*/
                    try {
                        dsl.startWebserver(nodeAFuture.get());
                        dsl.startWebserver(nodeB.get());
                        //            dsl.startWebserver(nodeC.get());
                        //           dsl.startWebserver(nodeD.get());
                    } catch (Throwable e) {
                        System.err.println("Encountered exception in node startup: " + e.getMessage());
                        e.printStackTrace();
                    }

                    dsl.waitForAllNodesToFinish();

                    return null;
                }
        );
    }
}