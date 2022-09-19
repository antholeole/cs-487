CC       = gcc
CFLAGS   = -Wall
LDFLAGS  =
OBJFILES = argparse.o main.o beacon_sender.o logger.o sender_udp.o cmd_agent.o
TARGET   = client

all: $(TARGET)

$(TARGET): $(OBJFILES)
	$(CC) $(CFLAGS) -o $(TARGET) $(OBJFILES) $(LDFLAGS)

clean:
	rm -f $(OBJFILES) $(TARGET) *~