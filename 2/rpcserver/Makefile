CC       = gcc
CFLAGS   = -Wall
LDFLAGS  =
OBJFILES = main.o
TARGET   = rpcserver

all: $(TARGET)

$(TARGET): $(OBJFILES)
	$(CC) $(CFLAGS) -o $(TARGET) $(OBJFILES) $(LDFLAGS)

clean:
	rm -f $(OBJFILES) $(TARGET) *~