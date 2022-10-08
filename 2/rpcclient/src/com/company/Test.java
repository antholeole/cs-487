package com.company;

import java.io.IOException;
import java.nio.ByteOrder;

import static com.company.c_char.FALSE;
import static com.company.c_char.TRUE;

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

        System.out.printf("GetLocalTime: time: %d, valid: %b\n", time, valid == TRUE);

        // test getLocalOS
        GetLocalOS obj2 = new GetLocalOS();
        obj2.valid.setValue(FALSE);
        obj2.execute(IP, PORT);

        String os = obj2.os.stream().map((c_char) -> c_char.get().toString()).reduce((acc, e) -> acc  + e).get();
        valid = obj2.valid.get();

        System.out.printf("GetLocalOS: os: %s, valid: %b\n", os, valid == TRUE);

    }
}
