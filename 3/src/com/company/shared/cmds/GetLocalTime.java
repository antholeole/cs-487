package com.company.shared.cmds;

import java.io.Serializable;

public class GetLocalTime implements Serializable {
    public static final String CODE = "GetLocalTime";

    public int valid = -1;
    public int localTime = -1;
}
