CC       = gcc
CFLAGS   = -Wall
LDFLAGS  = -c -fPIC -I /opt/homebrew/opt/openjdk/include
OBJFILES = com_company_agent_CmdAgent.o
TARGET   = CmdAgent

all:
	javac -h . CmdAgent.java && \
	gcc -c -fPIC -I /opt/homebrew/opt/openjdk/include/include com_company_agent_CmdAgent.c -o com_company_agent_CmdAgent.o && \
	gcc -dynamiclib -o libagent.dylib com_company_agent_CmdAgent.o -lc

clean:
	rm -f $(OBJFILES) $(TARGET) *~%