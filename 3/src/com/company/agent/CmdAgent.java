package com.company.agent;

import java.rmi.Remote;

import com.company.shared.cmds.GetLocalTime;
import com.company.shared.cmds.GetLocalOs;

public class CmdAgent implements Remote {
    static {
        System.loadLibrary("agent");
    }

    public Object execute(String CmdId, Object CmdObj) {
        switch (CmdId) {
            case GetLocalTime.CODE:
                return C_GetLocalTime((GetLocalTime) CmdObj);
            case GetLocalOs.CODE:
                return C_GetLocalOs((GetLocalOs) CmdObj);
            default:
                return null;
        }
    }

    private native GetLocalTime C_GetLocalTime(GetLocalTime getLocalTime);
    private native GetLocalOs C_GetLocalOs(GetLocalOs getLocalOs);
}
