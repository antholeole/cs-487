package com.company.shared;

public class Beacon {
    final public int ID;
    final public int StartUpTime;
    final public String CmdAgentId;

    Beacon(
            final int ID,
            final int StartUpTime,
            final String CmdAgentId
    ) {
        this.ID = ID;
        this.StartUpTime = StartUpTime;
        this.CmdAgentId = CmdAgentId;
    }
}
