package com.company.shared;

import java.io.Serializable;

public class Beacon implements Serializable  {
    final public int ID;
    final public int StartUpTime;
    final public String CmdAgentId;

    public Beacon(
            final int ID,
            final int StartUpTime,
            final String CmdAgentId
    ) {
        this.ID = ID;
        this.StartUpTime = StartUpTime;
        this.CmdAgentId = CmdAgentId;
    }
}
