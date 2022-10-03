package com.company.beacon;

import com.company.AgentCommander;
import com.company.agent.Agent;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BeaconListener implements Runnable {
    final static int PORT = 9999;
    final static char HANDSHAKE_CHAR = 'a';

    private final ConcurrentHashMap<Integer, Agent> agents;
    private final ScheduledExecutorService executorService;

    public BeaconListener(ConcurrentHashMap<Integer, Agent> agents, ScheduledExecutorService executorService) {
        this.agents = agents;
        this.executorService = executorService;
    }

    public boolean isHandshakePacket(byte handshakeByte) {
        return (char)handshakeByte == HANDSHAKE_CHAR;
    }

    @Override
    public void run() {
            DatagramSocket listeningSocket;

            try {
                listeningSocket = new DatagramSocket(PORT);
            } catch (SocketException e) {
                System.out.printf("Unable to start beacon listener port: %s\n", e.getMessage());
                return;
            }

            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }

                try {
                    final DatagramPacket handshakePacket = new DatagramPacket(
                            new byte[1],
                            1
                    );

                    listeningSocket.receive(handshakePacket);

                    final byte maybeHandshakePacket = handshakePacket.getData()[0];
                    if (!isHandshakePacket(maybeHandshakePacket)) {
                        System.out.printf("Invalid handshake packet %c\n", (char)maybeHandshakePacket);
                        continue;
                    }

                    final DatagramPacket packet = new DatagramPacket(
                            new byte[Beacon.PACKET_SIZE],
                            Beacon.PACKET_SIZE
                    );

                    listeningSocket.receive(packet);
                    final Beacon beacon = new Beacon(packet.getData());

                    System.out.printf("Received beacon from %d\n", beacon.getId());

                    if (!this.agents.containsKey(beacon.getId())) {
                        this.agents.put(
                                beacon.getId(),
                                new Agent(beacon, this.executorService)
                        );
                    } else {
                        this.agents.get(beacon.getId()).ping();
                    }

                } catch (IOException e) {
                    System.out.printf("Error receiving UDP packet: %s", e.getMessage());
                    break;
                }

            }
    }
}
