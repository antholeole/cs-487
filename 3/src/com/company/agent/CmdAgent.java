package com.company.agent;

import java.rmi.Remote;

public class CmdAgent implements Remote {
    static {
        System.load("/Users/anthony/Projects/school/487/3/src/com/company/agent/libagent.dylib");
    }

    public Object execute(String CmdId, Object CmdObj) {
        C_GetLocalTime();
        return "hi";
    }

    private native void C_GetLocalTime();
}
