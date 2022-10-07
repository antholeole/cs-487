package com.company;

import java.io.IOException;
import java.nio.ByteOrder;

import static com.company.c_char.FALSE;

public class Test {
    static ByteOrder protocolByteOrder = ByteOrder.BIG_ENDIAN;

    static String IP = "127.0.0.1";
    static int PORT = 8794;

    public static void main(String[] args) throws IOException {
        GetLocalTime obj = new GetLocalTime();
        obj.valid.setValue(FALSE);
        obj.execute(IP, PORT);

        Integer time = obj.time.get();
        Character valid = obj.valid.get();

        System.out.printf("GetLocalTime: time: %d, valid: %c\n", time, valid);

    }
}
