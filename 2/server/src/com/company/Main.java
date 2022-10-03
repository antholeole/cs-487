package com.company;

import com.company.agent.Agent;
import com.company.beacon.BeaconListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Main {
    public static void main(String[] args) {
        ConcurrentHashMap<Integer, Agent> agents = new ConcurrentHashMap<>();

        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        final BeaconListener beaconListener = new BeaconListener(agents, executorService);

        final Thread thread = new Thread(beaconListener);
        thread.start();
    }
}
