package com.company.beacon;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Beacon {
    int id;
    long startupTime;
    int timeInterval;
    byte[] Ip;
    int cmdPort;

    public static final int PACKET_SIZE = 20;

    public Beacon(byte[] bytes) {
        final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        byteBuffer.order(ByteOrder.BIG_ENDIAN);

        this.id = byteBuffer.getInt();
        this.startupTime = byteBuffer.getInt();
        this.timeInterval = byteBuffer.getInt();
        this.Ip = new byte[]{
                byteBuffer.get(),
                byteBuffer.get(),
                byteBuffer.get(),
                byteBuffer.get()
        };
        this.cmdPort = byteBuffer.getInt();
    }

    public int getId() {
        return this.id;
    }

    public byte[] getIp() {
        return Ip;
    }

    public int getCmdPort() {
        return cmdPort;
    }

    public int getTimeInterval() {
        return this.timeInterval;
    }
}
