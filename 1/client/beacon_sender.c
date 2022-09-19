#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include <arpa/inet.h>

#include "beacon_sender.h"

void *spawnBeaconSender(void *args) {
    threadArgs* tArgs = (threadArgs*) args;

    beacon* beacon = malloc(sizeof(beacon));

    beacon->id = tArgs->args->id;
    beacon->startup_time = tArgs->startup_time;
    beacon->time_interval = tArgs->args->ping_interval_s;
    beacon->cmd_port = tArgs->args->port;
    int i;
    for (i = 0; i < 4; i++) {
        beacon->ip[i] = tArgs->args->host[i];
    }

    // Create socket:
    int socket_desc = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);

    if(socket_desc < 0){
        printf("Error while creating socket\n");
        return NULL;
    }

    while (1) {
        send_to_manager(beacon, socket_desc);
        log_msg_info(tArgs->logger, BeaconSender, "sent to manager (@ time %d)", time(NULL));
        sleep(tArgs->args->ping_interval_s);
    }

    close(socket_desc);
    free(beacon);
    return NULL;
}