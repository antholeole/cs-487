package com.company.manager;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.company.agent.CmdAgent;
import com.company.shared.Beacon;
import com.company.shared.cmds.GetLocalTime;

public class ManagerImpl implements Manager {
    private Map<Integer, Long> beacons = new HashMap<>();

    public static int registryPort = 8080;
    public static String managerRegistryTag = "MANAGER";
    public static int deadSeconds = 10;

    public Registry registry = null;

    public static void main(String[] args) throws RemoteException, AlreadyBoundException, AccessException {
        ManagerImpl manager = new ManagerImpl();
        Manager stub = (Manager) UnicastRemoteObject.exportObject(manager, 0);

        manager.registry = LocateRegistry.createRegistry(registryPort);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                for (Map.Entry<Integer, Long> entry : manager.beacons.entrySet()) {
                    if (entry.getValue() != null && entry.getValue() + deadSeconds < Instant.now().getEpochSecond()) {
                        manager.beacons.put(entry.getKey(), null);
                        System.out.printf("Client %s died.\n", entry.getKey());
                    }
                }
            }
        }, 0, 3, TimeUnit.SECONDS);

        manager.registry.bind(managerRegistryTag, stub);
    }

    public int deposit(Beacon b) throws RemoteException {
        System.out.printf("Recieved beacon from %s.\n", b.ID);

        if (beacons.containsKey(b.ID) && beacons.get(b.ID) == null) {
            System.out.printf("%s has been revived!\n", b.ID);
        } else {
            beacons.put(b.ID, Instant.now().getEpochSecond());
            try {
                final CmdAgent cmdAgent = (CmdAgent) this.registry.lookup(b.CmdAgentId);
                final GetLocalTime getLocalTime = (GetLocalTime) cmdAgent.execute(GetLocalTime.CODE, new GetLocalTime());
                System.out.printf("%s local time: %i\n", b.ID, getLocalTime.localTime);
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }

        return 1;
    }
}
