#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <netdb.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <time.h>

#include "constants.h"

#define BUFSIZE 1

enum CmdType {
    GetLocalOs,
    GetLocalTime
};

#ifdef _WIN32
    #define OS "Windows 32-bit"
#elif _WIN64
    #define OS "Windows 64-bit"
#elif __APPLE__ || __MACH__
    #define OS "Mac OSX"
#elif __linux__
    #define OS "Linux"
#elif __FreeBSD__
    #define OS "FreeBSD"
#elif __unix || __unix__
    #define OS "Unix"
#else
    #define OS "Other"
#endif

int sendLocalOs(int sockfd, logger* loggr) {
    int buf_size = strlen(OS) + 2;
    // extra room for null byte and length flag
    char buf[buf_size];

    uint8_t os_len = strlen(OS);
    memcpy(&buf[0], &os_len, 1);
    strcpy(&buf[1], OS);

    int writeout = write(sockfd, buf, buf_size);

    if (writeout < 0) {
        return -1;
    } else {
        log_msg_info(loggr, CmdAgent, "Sent local OS (%s)!", OS);
        return 1;
    }
}

int sendLocalTime(int sockfd, logger* loggr) {
    char buf[4];

    int32_t local_time = time(NULL);
    local_time = htonl(local_time);
    memcpy(&buf[0], &local_time, 4);

    int writeout = write(sockfd, buf, 4);

    if (writeout < 0) {
        return -1;
    } else {
        log_msg_info(loggr, CmdAgent, "Sent local time (%d)!", ntohl(local_time));
    }
}

int socket_listen_loop(int incomingfd, threadArgs* tArgs) {
    int socket_open = 0;
    while (socket_open >= 0) {
        char buf[BUFSIZE];
        bzero(buf, BUFSIZE);
        int n = read(incomingfd, buf, 32);
        if (n < 0) {
            break;
        }

        // only one byte; no worries for endianness
        int8_t cmd_option = buf[0];

        switch (cmd_option) {
            case GetLocalOs:
                socket_open = sendLocalOs(incomingfd, tArgs->logger);
                break;
            case GetLocalTime:
                socket_open = sendLocalTime(incomingfd, tArgs->logger);
                break;
            default:
                log_msg_info(tArgs->logger, CmdAgent, "Recieved an unknown command %d", cmd_option);
                break;
        }
    }
}

void* spawnCmdAgent(void* args) {
    threadArgs* tArgs = (threadArgs*)args;

    int serverfd = socket(AF_INET, SOCK_STREAM, 0);
    if (serverfd < 0) {
        log_msg_fatal(tArgs->logger, CmdAgent, "Error opening socket");
    }

    int optval = 1;
    setsockopt(serverfd, SOL_SOCKET, SO_REUSEADDR,
        (const void*)&optval, sizeof(int));

    struct sockaddr_in serveraddr;
    serveraddr.sin_family = AF_INET;
    serveraddr.sin_addr.s_addr = htonl(INADDR_ANY);
    serveraddr.sin_port = htons((unsigned short)tArgs->args->port);

    if (bind(serverfd, (struct sockaddr*)&serveraddr, sizeof(serveraddr)) < 0) {
        log_msg_fatal(tArgs->logger, CmdAgent, "Error on binding");
    }

    if (listen(serverfd, 5) < 0) {
        log_msg_fatal(tArgs->logger, CmdAgent, "Error opening socket");
    }

    struct sockaddr_in clientaddr;
    int clientlen = sizeof(clientaddr);

    while (1) {
        int incomingfd = accept(serverfd, (struct sockaddr*)&clientaddr, &clientlen);
        if (incomingfd < 0) {
            log_msg_fatal(tArgs->logger, CmdAgent, "Unable to accept incoming connection");
        }

        char* hostaddrp = inet_ntoa(clientaddr.sin_addr);
        if (hostaddrp == NULL) {
            log_msg_fatal(tArgs->logger, CmdAgent, "Unable to look up host info");
        }

        //16 is enough for xxx.xxx.xxx.xxx
        char *required_host_addr = (char*)malloc(18 * sizeof(char));
        sprintf(
            required_host_addr,
            "%d.%d.%d.%d",
            tArgs->args->host[0],
            tArgs->args->host[1],
            tArgs->args->host[2],
            tArgs->args->host[3]
        );

        if (strcmp(hostaddrp, required_host_addr) != 0) {
            log_msg_info(tArgs->logger, CmdAgent, "server established connection an incorrect client %s (wanted %s). closing", hostaddrp, required_host_addr);
            close(incomingfd);
        } else {
            log_msg_info(tArgs->logger, CmdAgent, "server established connection with %s", hostaddrp);
        }


        socket_listen_loop(incomingfd, tArgs);
        log_msg_info(tArgs->logger, CmdAgent, "server closed TCP connection. waiting on new...", hostaddrp);
    }
}

