#ifndef CONSTANTS_H
#define CONSTANTS_H

#include "argparse.h"
#include "logger.h"
#include <stdint.h>

#define MANAGER_PORT 9999
#define BEACON_POLL_TIME 1


typedef struct ThreadArgs {
    struct Args *args;
    struct Logger *logger;
    int32_t startup_time;
} threadArgs;

#endif