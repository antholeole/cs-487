package com.company.agent;

import java.rmi.Remote;

import com.company.shared.cmds.GetLocalTime;

public class CmdAgent implements Remote {
    static {
        System.loadLibrary("agent");
    }

    public Object execute(String CmdId, Object CmdObj) {
        switch (CmdId) {
            case "GetLocalTime":
                return C_GetLocalTime((GetLocalTime) CmdObj);
            default:
                return null;
        }
    }

    private native GetLocalTime C_GetLocalTime(GetLocalTime getLocalTime);
}
