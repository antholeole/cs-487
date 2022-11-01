package com.company.agent;

import java.rmi.RemoteException;

public interface CmdAgent extends java.rmi.Remote {
    public Object execute(String CmdId, Object CmdObj) throws RemoteException;
}
