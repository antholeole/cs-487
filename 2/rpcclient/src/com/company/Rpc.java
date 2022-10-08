package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.List;

abstract public class Rpc {
    static Integer rpcTypeCodeLength = 100;
    static Integer rpcBodySizeLength = 4;

    // order of param matters
    abstract protected List<c_type<?>> getBody();
    abstract protected String getRpcTypeCode();

    private Integer bodySize() {
        return getBody()
                .stream()
                .map(c_type::getSize)
                .reduce(0, Integer::sum);
    }

    //builds both the header and content payload
    private ByteBuffer buildBuffer() {
        List<c_type<?>> rpcBody = getBody();

        // allocate size for body and then also rpc id, and size
        final ByteBuffer bodyBuffer = ByteBuffer.allocate(
                bodySize()
                        + rpcTypeCodeLength
                        + rpcBodySizeLength
        );
        bodyBuffer.order(Test.protocolByteOrder);

        // write RPC code
        final String rpcCode = getRpcTypeCode();
        for (int i = 0; i < rpcCode.length(); i++) {
            bodyBuffer.put((byte) rpcCode.charAt(i));
        }
        bodyBuffer.put((byte) '\0');
        bodyBuffer.position(rpcTypeCodeLength);

        // write body size
        bodyBuffer.putInt(bodySize());

        // write body (each of the values, enumerated)
        rpcBody.forEach((v) -> bodyBuffer.put(v.getBytes()));
        bodyBuffer.position(0);

        return bodyBuffer;
    }

    public void execute(String ip, int port) throws IOException {
        Socket socket;

        socket = new Socket(ip, port);

        final OutputStream output = socket.getOutputStream();
        InputStream input = socket.getInputStream();

        final ByteBuffer byteBuffer = buildBuffer();
        output.write(byteBuffer.array());

        // move buffer to after initial flags
        byteBuffer.position(rpcBodySizeLength + rpcTypeCodeLength);

        // read bytes from client
        byte[] bodyBytes = new byte[bodySize()];
        input.read(bodyBytes);

        // put it into our buffer
        byteBuffer.put(bodyBytes);
        byteBuffer.position(rpcBodySizeLength + rpcTypeCodeLength);

        // fill our own body
        loadFromBuffer(byteBuffer);

        socket.close();
    }

    public void loadFromBuffer(ByteBuffer loadedBuffer) {
        getBody().forEach(v -> v.setFromBytes(loadedBuffer));
    }
}
