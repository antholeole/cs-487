package com.company.agent;

import com.company.AgentCommander;
import com.company.beacon.Beacon;

import java.util.concurrent.*;

public class Agent {
    private final int id;
    private final int secondsBetweenPings;
    private final int cmdPort;
    private final byte[] ip;

    private AgentStatus status = AgentStatus.healthy;
    private final ScheduledExecutorService executorService;

    private ScheduledFuture<?> decayFuture;

    public Agent(final Beacon beacon, final ScheduledExecutorService executorService) {
        this.id = beacon.getId();
        this.cmdPort = beacon.getCmdPort();
        this.ip = beacon.getIp();

        this.secondsBetweenPings = beacon.getTimeInterval();
        this.executorService = executorService;

        getAgentVitals();
        scheduleDecay();
    }

    void getAgentVitals() {
        new Thread(new AgentCommander(id, cmdPort, ip)).start();
    }

    public AgentStatus getStatus() {
        return this.status;
    }

    public void ping() {
        this.decayFuture.cancel(true);

        if (this.status == AgentStatus.missing) {
            System.out.printf("%d has been revived! (Getting vitals)\n", this.id);
            getAgentVitals();
        }

        this.status = AgentStatus.healthy;
        scheduleDecay();
    }

    private void scheduleDecay() {
        this.decayFuture = executorService.schedule(
                () -> {
                    System.out.printf("%d has died.\n", this.id);
                    this.status = AgentStatus.missing;
                },
                this.secondsBetweenPings * 2L,
                TimeUnit.SECONDS
        );
    }

    public int getId() {
        return this.id;
    }
}
