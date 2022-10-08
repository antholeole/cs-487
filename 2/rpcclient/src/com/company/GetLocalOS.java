package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GetLocalOS extends Rpc {
    List<c_char> os = Arrays.stream(new c_char[16])
            .map((v) -> new c_char())
            .collect(Collectors.toList());
    c_char valid = new c_char();

    @Override
    protected List<c_type<?>> getBody() {
        List<c_type<?>> body = new ArrayList<>(os);
        body.add(valid);
        return body;
    }

    @Override
    protected String getRpcTypeCode() {
        return "GetLocalOS";
    }
}
