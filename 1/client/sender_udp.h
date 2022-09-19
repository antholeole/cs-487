#ifndef SENDER_UDP_H
#define SENDER_UDP_H

#include "constants.h"

#include <stdint.h>

#define BEACON_PACKET_SIZE 20
typedef struct Beacon {
    int16_t id;
    int32_t startup_time;
    int32_t cmd_port;
    int time_interval;
    int8_t ip[4];
} beacon;

void send_to_manager(beacon *b, int socket_desc);

#endif
