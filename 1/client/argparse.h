#ifndef ARGPARSE_H
#define ARGPARSE_H

typedef struct Args {
    int port;
    int log_beacon_sender;
    int log_cmd_agent;
    int id;
    int ping_interval_s;
    char host[3];
} args;

struct Args* parse(int argc, char* argv[]);

#endif