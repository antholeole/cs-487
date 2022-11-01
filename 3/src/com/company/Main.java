package com.company;

import com.company.agent.CmdAgent;
import com.company.shared.cmds.GetLocalOs;

public class Main {
    public static void main(String[] args) {
        final GetLocalOs getLocalOs = new GetLocalOs();


        new CmdAgent().execute(GetLocalOs.CODE, getLocalOs);

        System.out.println(getLocalOs.localOs);
    }
}
