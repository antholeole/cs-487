#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#include "argparse.h"

#define PORT_MAX 65353
#define PORT_MIN 1025

struct Args* parse(int argc, char* argv[]) {
    struct Args *a = malloc(sizeof(struct Args));

    // default values
    a->log_beacon_sender = 0;
    a->log_cmd_agent = 0;
    a->port = rand() % (PORT_MAX - PORT_MIN) + PORT_MIN;
    a->id = rand();
    a->ping_interval_s = 15;

    int argIdx;
    for (argIdx = 1; argIdx < argc; argIdx++) {
        if (!strcmp(argv[argIdx], "-p")) {
            a->port = atoi(argv[argIdx + 1]);
        }

        if (!strcmp(argv[argIdx], "-i")) {
            a->id = atoi(argv[argIdx + 1]);
        }

        if (!strcmp(argv[argIdx], "-b")) {
            a->ping_interval_s = atoi(argv[argIdx + 1]);
        }

        if (!strcmp(argv[argIdx], "-bs")) {
            a->log_beacon_sender = 1;
        }

        if (!strcmp(argv[argIdx], "-ca")) {
            a->log_cmd_agent = 1;
        }

        if (!strcmp(argv[argIdx], "-mh")) {
            int i;
            for (i = 0; i < 4; i++) {
                a->host[i] = atoi(argv[argIdx + 1 + i]);
            }
        }
        if (!strcmp(argv[argIdx], "-sh")) {
            int i;
            for (i = 0; i < 4; i++) {
                a->host[i] = atoi(argv[argIdx + 1 + i]);
            }
        }
    }

    return a;
}