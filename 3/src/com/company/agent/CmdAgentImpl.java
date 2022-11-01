package com.company.agent;

import java.nio.file.Paths;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.company.shared.cmds.GetLocalTime;
import com.company.manager.Manager;
import com.company.manager.ManagerImpl;
import com.company.shared.Beacon;
import com.company.shared.cmds.GetLocalOs;

public class CmdAgentImpl implements CmdAgent {
    static {
        // I know I can just run with this in javapath, but this is easier for testing.
        final String libPath = Paths.get("src/com/company/agent/libagent.so").toAbsolutePath().toString();
        System.out.println(libPath);
        System.load(libPath);
    }

    final static int beaconInterval = 5; //seconds
    final public Integer CmdAgentId = (int)(Math.random() * 100);

    public static void main(String[] args) throws RemoteException, NotBoundException, AlreadyBoundException {
        final Registry registry = LocateRegistry.getRegistry(ManagerImpl.registryPort);
        final CmdAgentImpl cmdAgent = new CmdAgentImpl();

        System.out.printf("Booting %s at %i.\n",
            cmdAgent.C_GetLocalOs(new GetLocalOs()).localOs,
            cmdAgent.C_GetLocalTime(new GetLocalTime()).localTime
        );

        Manager manager = (Manager) registry.lookup(ManagerImpl.managerRegistryTag);

        CmdAgent cmdAgentStub = (CmdAgent) UnicastRemoteObject.exportObject(cmdAgent, 0);
        registry.bind(cmdAgent.CmdAgentId.toString(), cmdAgentStub);


        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    manager.deposit(
                        new Beacon(
                            cmdAgent.CmdAgentId,
                            (int) Instant.now().getEpochSecond(),
                            cmdAgent.CmdAgentId.toString()
                        )
                    );
                } catch (RemoteException e) {
                    System.err.println("Failed to add beacon to manager: ");
                    e.printStackTrace();
                }
            }
        }, 0, beaconInterval, TimeUnit.SECONDS);
    }

    public Object execute(String CmdId, Object CmdObj) throws RemoteException {
        switch (CmdId) {
            case GetLocalTime.CODE:
                return C_GetLocalTime((GetLocalTime) CmdObj);
            case GetLocalOs.CODE:
                return C_GetLocalOs((GetLocalOs) CmdObj);
            default:
                return null;
        }
    }

    private native GetLocalTime C_GetLocalTime(GetLocalTime getLocalTime);
    private native GetLocalOs C_GetLocalOs(GetLocalOs getLocalOs);
}
