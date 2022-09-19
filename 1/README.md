### Running:
To run the client:
- `cd client`
- `make && ./client`

client accepts the following flags:
`--ping-interval`: (int, seconds) how long to wait between beacon pings. Defaults to 60
`--log-command-agent` ("Command Agent"): (none) enables logging for the command agent
`--log-beacon-sender` ("Beacon Sender"): (none) enables logging for the beacon sender
`--id` ("id"): (int) what ID to assign the current client
`--tcp-port` ("port"): (int) what port to accept the incoming TCP connection on
`--manager-host` ("manager host") (int int int int) what host the manager is on
`--manager-port` ("manager port") (int) what port the manager is one
`--self-host` ("self host") (int int int int) the host of the current agent (so we can tell the manager where to find our public IP)

A full run on localhost looks like the following:
`make && ./client --ping-interval 5 --log-command-agent --log-beacon-sender --manager-host 10 90 4 211 --manager-port 9999 --self-host 10 90 4 211 --tcp-port 8888 --id 20`
This will start a agent that pings every 5 seconds, runs both loggers, has a manager on host 10.90.4.211:9999 and is running on 10:90:4:211 with port 8888 open for commands and an id of 20.


To run the server: I have precompiled the java code into a `jar` file for ease of use. simply:
- `cd server`
- `java -jar server.jar`

This will start the server, listening for UDP on port 9999.
