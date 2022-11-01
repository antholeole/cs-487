package com.company.shared.cmds;

import java.io.Serializable;

public class GetLocalOs implements Serializable {
    public static final String CODE = "GetLocalOs";

    public int valid = -1;
    public String localOs = "";
}
