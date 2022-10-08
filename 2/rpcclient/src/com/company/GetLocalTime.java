package com.company;

import java.util.Arrays;
import java.util.List;

public class GetLocalTime extends Rpc {
    c_int time = new c_int();
    c_char valid = new c_char();

    @Override
    protected List<c_type<?>> getBody() {
        return Arrays.asList(time, valid);
    }

    @Override
    protected String getRpcTypeCode() {
        return "GetLocalTime";
    }
}
