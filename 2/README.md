For your testing convienence, the server runs on localhost so that you don't have to fiddle with the code at all to run. Also, I've pre-packaged a JAR for you so you don't have to compile either.

to run, from the base directory:

1. Start server: `cd rpcserver && make && ./rpcserver`
2. Run client: `java -jar rpcclient/out/artifacts/rpcclient_jar/rpcclient.jar`

You should see something like:

```
GetLocalTime: time: 1665245902, valid: true
GetLocalOS: os: Mac OSX, valid: 1
```

if running on macOS.