package com.company;

import java.nio.ByteBuffer;

abstract public class c_type<T> {
    abstract public ByteBuffer getBytes();
    abstract public T get();
    abstract public void setValue(T val);
    abstract public void setFromBytes(ByteBuffer byteBuffer);

    protected ByteBuffer getSizedBuffer() {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(getSize());
        byteBuffer.order(Test.protocolByteOrder);
        return byteBuffer;
    }

    protected abstract int getSize();
}
