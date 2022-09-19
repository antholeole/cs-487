package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class AgentCommander implements Runnable {
    private enum AgentCommands {
        GetLocalOs,
        GetLocalTime
    }

    final String host;
    final int port;
    final int agentId;

    public AgentCommander(
            final int agentId,
            final int port,
            final byte[] host
    ) {
        this.port = port;
        this.agentId = agentId;

        final StringBuilder hostBuilder = new StringBuilder();

        for (int i = 0; i < host.length; i++) {
            hostBuilder.append(((int) host[i]) & 0xFF);

            if (i != host.length - 1) {
                hostBuilder.append('.');
            }
        }

        this.host = hostBuilder.toString();
    }

    void createConnection() throws IOException {
        Socket socket;
    }

    @Override
    public void run() {
        Socket socket;

        try {
            socket = new Socket(this.host, this.port);
        } catch (IOException e) {
            System.out.printf("Could not open TCP connection to %s:%d\n", this.host, this.port);
            e.printStackTrace();
            return;
        }
        OutputStream output;
        InputStream input;

        try {
            output = socket.getOutputStream();
            input = socket.getInputStream();
        } catch (IOException e) {
            System.out.printf("Could get I/O streams to %s:%d\n", this.host, this.port);
            e.printStackTrace();
            return;
        }

        try {
            System.out.printf("Requesting %d's os...\n", agentId);
            final String clientOs = getClientOs(output, input);
            System.out.printf("%d's os: %s\n", agentId, clientOs);
        } catch (IOException e) {
            System.out.println("Failed to get client OS.");
            e.printStackTrace();
            return;
        }

        try {
            System.out.printf("Requesting %d's time...\n", agentId);
            final int clientTime = getClientTime(output, input);
            System.out.printf("%d's local time: %d\n", agentId, clientTime);
        } catch (IOException e) {
            System.out.println("Failed to get client time.");
            e.printStackTrace();

            return;
        }

        try {
            socket.close();
            System.out.printf("Got client %d time and OS; socket closed.\n", agentId);
        } catch (IOException e) {
            System.out.println("Unable to close socket.");
            e.printStackTrace();
        }

    }

    private String getClientOs(OutputStream output, InputStream input) throws IOException {
        output.write(AgentCommands.GetLocalOs.ordinal());

        final byte[] flagBytes = input.readNBytes(1);
        final int flag = Byte.toUnsignedInt(flagBytes[0]);
        final byte[] osBytes = input.readNBytes(flag);
        //Don't need the null byte
        input.skip(1);

        return new String(osBytes, StandardCharsets.UTF_8);
    }

    private int getClientTime(OutputStream output, InputStream input) throws IOException {
        output.write(AgentCommands.GetLocalTime.ordinal());
        final ByteBuffer byteBuffer = ByteBuffer.wrap(input.readNBytes(4));
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
        return byteBuffer.getInt();
    }
}
