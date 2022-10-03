#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <fcntl.h>
#include <netdb.h>
#include <sys/time.h>
#include <time.h>
#include <arpa/inet.h>
#include <errno.h>

#include "sender_udp.h"


void send_to_manager(beacon *b, int socket_desc) {
    int8_t MANAGER_HOST[5] = { 127, 0, 0, 1 };
    struct sockaddr_in server_addr;

    char beacon_serialized[BEACON_PACKET_SIZE];

    int32_t id = htonl(b->id);
    memcpy(beacon_serialized, &id, 4);

    //TODO: 64? WHY
    int32_t startup_time = htonl(b->startup_time);
    memcpy(beacon_serialized + 4, &startup_time, 4);

    int32_t time_interval = htonl(b->time_interval);
    memcpy(beacon_serialized + 8, &time_interval, 4);

    memcpy(beacon_serialized + 12, &b->ip[0], 1);
    memcpy(beacon_serialized + 13, &b->ip[1], 1);
    memcpy(beacon_serialized + 14, &b->ip[2], 1);
    memcpy(beacon_serialized + 15, &b->ip[3], 1);

    int32_t cmdPort = htonl(b->cmd_port);
    memcpy(beacon_serialized + 16, &cmdPort, 4);

    // Set port and IP:
    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(MANAGER_PORT);

    char ip[16];
    sprintf(
        ip,
        "%03d.%03d.%03d.%03d",
        MANAGER_HOST[0],
        MANAGER_HOST[1],
        MANAGER_HOST[2],
        MANAGER_HOST[3]
    );

    server_addr.sin_addr.s_addr = inet_addr(ip);

    // Send the message to server:
    char handshake[1] = {'a'};
       sendto(
        socket_desc,
        handshake,
        sizeof(handshake),
        0, (struct sockaddr*)&server_addr,
        sizeof(server_addr)
    );

    sendto(
        socket_desc,
        beacon_serialized,
        sizeof(beacon_serialized),
        0, (struct sockaddr*)&server_addr,
        sizeof(server_addr)
    );
}