package com.company;

import java.nio.ByteBuffer;

public class c_char extends c_type<Character> {
    public static Character TRUE = '1';
    public static Character FALSE = '0';

    private Character javaChar;

    @Override
    public ByteBuffer getBytes() {
        final ByteBuffer byteBuffer = getSizedBuffer();
        byteBuffer.putChar(javaChar);
        return byteBuffer;
    }

    @Override
    public Character get() {
        return javaChar;
    }

    @Override
    public void setValue(Character val) {
        this.javaChar = val;
    }

    @Override
    public void setFromBytes(ByteBuffer byteBuffer) {
        this.javaChar = byteBuffer.getChar();
    }


    @Override
    protected int getSize() {
        return 2;
    }
}
