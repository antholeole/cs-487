# generate headers
javac -sourcepath src -h src/com/company/agent src/com/company/agent/CmdAgent.java

#compile c
gcc -c -fPIC -I /usr/lib/jvm/java-11-openjdk-11.0.16.1.1-1.fc35.x86_64/include -I /usr/lib/jvm/java-11-openjdk-11.0.16.1.1-1.fc35.x86_64/include/linux src/com/company/agent/com_company_agent_CmdAgent.c -o src/com/company/agent/com_company_agent_CmdAgent.o

# link c to dynalib
gcc -shared -fPIC -o src/com/company/agent/libagent.so src/com/company/agent/com_company_agent_CmdAgent.o -lc

# compile all java
find . -type f -path "./src/*/*" -name "*.class" -exec rm -f {} \; # delete all old class files
touch src/com/company/Main.java # re-traverse the depedency graph
javac -sourcepath src src/com/company/Main.java

java -cp src/ -Djava.library.path="src/com/company/agent" com.company.Main