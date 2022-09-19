### DESIGN

Server is assumed to be running at the host + port provided in the command line arguements to the client. For ease of use, the jar is packaged with the server listening for the UDP on port `9999`.

Flow:
1. client starts up. Sends a ping to the provided server (through arguements).
2. Server recieves ping; checks if the ping has the correct "handshake" packet. If it does, proceed. if it doesn't, reject.
3. If this is a new client, request the vitals (time and OS): server opens a TCP connection with client on the specified port (port given to server through beacon).
4. The client is listening on said port, but verifies the host of incoming requests: if the host is not equal to the one that it is communicating with (through UDP), then it rejects the connection.
5. Server sends commands to client. Client responds through the same TCP connection.
6. When the server is done checking vitals, it closes the connection. The client then waits for a new connection in the future.

- The client will send beacons to the server, regardless of reception status. It will log if the server recieved the beacon or not.
- All wire communication is done using network-endian.
- The beacon is bit-packed; since the server knows the structure of the beacon, it knows what bits to read without having to specify bit length. The only place bit length is specified is when the server asks the client for its OS.

