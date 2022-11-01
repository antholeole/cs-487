JAVA_V="jre-11-openjdk-11.0.14.1.1-5.fc35.x86_64"

# generate headers
javac -sourcepath src -h src/com/company/agent src/com/company/agent/CmdAgent.java

#compile c
gcc -c -fPIC -I /usr/lib/jvm/${JAVA_V}/include -I /usr/lib/jvm/${JAVA_V}/include/linux src/com/company/agent/com_company_agent_CmdAgent.c -o src/com/company/agent/com_company_agent_CmdAgent.o

# link c to dynalib
gcc -shared -fPIC -o src/com/company/agent/libagent.so src/com/company/agent/com_company_agent_CmdAgent.o -lc