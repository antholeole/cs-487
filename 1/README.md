### Running:
To run the client:
- `cd client`
- `make && ./client`

client accepts the following flags:
`-b`: (int, seconds) how long to wait between beacon pings. Defaults to 60
`-ca` ("Command Agent"): (none) enables logging for the command agent
`-bs` ("Beacon Sender"): (none) enables logging for the beacon sender
`-i` ("id"): (int) what ID to assign the current client
`-p` ("port"): (int) what port to accept the incoming TCP connection on
`mh` ("manager host") (int int int int) what host the manager is on

A full run on localhost looks like the following:
`make && ./client -b 15 -ca -bs -i 10 -p 5000 -mh 127 0 0 1`

To run the server