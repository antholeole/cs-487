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

#include "endian.h"


#define PORT 8794
#define CMD_ID_BYTES_LEN 100
#define BODY_SIZE_BYTES_LEN 4

#define GET_LOCAL_TIME_RPC_CODE "GetLocalTime"
#define GET_LOCAL_OS_RPC_CODE "GetLocalOS"

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

typedef struct {
    int32_t time;
    char valid;
} GET_LOCAL_TIME;

typedef struct {
    char  os[16];
    char  valid;
} GET_LOCAL_OS;

void GetLocalOS(GET_LOCAL_OS* ds) {
    strcpy(ds->os, OS);
    ds->valid = '1';
}

void GetLocalTime(GET_LOCAL_TIME* ds) {
    int32_t local_time = time(NULL);
    printf("Local time is %d", local_time);

    local_time = htobe32(local_time);
    memcpy(&ds->time, &local_time, 4);
    ds->valid = '1';
}


#define GET_LOCAL_OS_RPC_CODE "GetLocalOS"



int main(int argc, char* argv[]) {
    printf("starting RPC server.\n");

    int serverfd = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);

    if (serverfd < 0) {
        printf("Error opening socket\n");
        exit(1);
    }

    int optval = 1;
    setsockopt(serverfd, SOL_SOCKET, SO_REUSEADDR,
        (const void*)&optval, sizeof(int));

    struct sockaddr_in serveraddr;

    serveraddr.sin_family = AF_INET;
    serveraddr.sin_addr.s_addr = htonl(INADDR_ANY);
    serveraddr.sin_port = htons((unsigned short)PORT);

    if (bind(serverfd, (struct sockaddr*)&serveraddr, sizeof(serveraddr)) < 0) {
        printf("Error on binding\n");
        exit(1);
    }

    if (listen(serverfd, 5) < 0) {
        printf("Error opening socket\n");
        exit(1);
    }

    struct sockaddr_in clientaddr;
    int clientlen = sizeof(clientaddr);

    while (1) {
        printf("awaiting connection...\n");
        int incomingfd = accept(serverfd, (struct sockaddr*)&clientaddr, (socklen_t*)&clientlen);
        printf("got connection.\n");

        if (incomingfd < 0) {
            printf("Unable to accept incoming connection\n");
            exit(1);
        }

        char header[CMD_ID_BYTES_LEN + BODY_SIZE_BYTES_LEN];
        bzero(header, CMD_ID_BYTES_LEN + BODY_SIZE_BYTES_LEN);


        int n = read(incomingfd, &header, CMD_ID_BYTES_LEN + BODY_SIZE_BYTES_LEN);
        if (n < 0) {
            break;
        }

        uint32_t payload_size = (header[100] << 24) + (header[101] << 16) + (header[102] << 8) + header[103];

        uint8_t buf[payload_size];
        n = read(incomingfd, &buf, payload_size);
        if (n < 0) {
            break;
        }


        if (strcmp(header, GET_LOCAL_TIME_RPC_CODE) == 0) {
            printf("client requested local time.\n");
            GetLocalTime((GET_LOCAL_TIME*)buf);
        }
        else if ((strcmp(header, GET_LOCAL_OS_RPC_CODE) == 0)) {
            printf("client requested local OS.\n");
            GetLocalOS((GET_LOCAL_OS*)buf);
        }
        else {
            printf("invalid RPC code: %s.\n", header);
        }

        int writeout = write(incomingfd, buf, payload_size);

        close(incomingfd);
    }

}