package com.company;

import java.rmi.RemoteException;

import com.company.agent.CmdAgentImpl;
import com.company.shared.cmds.GetLocalOs;

public class Main {
    public static void main(String[] args) throws RemoteException {
        final GetLocalOs getLocalOs = new GetLocalOs();


        new CmdAgentImpl().execute(GetLocalOs.CODE, getLocalOs);

        System.out.println(getLocalOs.localOs);
    }
}
