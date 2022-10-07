package com.company;

import java.nio.ByteBuffer;

public class c_int extends c_type<Integer> {
    private Integer value;

    @Override
    public ByteBuffer getBytes() {
        final ByteBuffer byteBuffer = getSizedBuffer();
        byteBuffer.putInt(this.value);
        return byteBuffer;
    }

    @Override
    public Integer get() {
        return value;
    }

    @Override
    public void setValue(Integer val) {
        this.value = val;
    }

    @Override
    public void setFromBytes(ByteBuffer byteBuffer) {
        this.value = byteBuffer.getInt();
    }

    @Override
    protected int getSize() {
        return 4;
    }
}
