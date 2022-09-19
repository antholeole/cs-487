#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <pthread.h>

#include "beacon_sender.h"
#include "cmd_agent.h"

#include "argparse.h"
#include "constants.h"
#include "logger.h"

int main(int argc, char* argv[]) {
    srand(time(NULL));

    struct Args *args = parse(argc, argv);

    logger logger;
    logger.shouldLogBeaconSender = args->log_beacon_sender;
    logger.shouldLogCmdAgent = args->log_cmd_agent;

    pthread_t beacon_sender;
    pthread_t cmd_agent;

    threadArgs *threadArgs = malloc(sizeof(threadArgs));
    threadArgs->args = args;
    threadArgs->logger = &logger;

    threadArgs->startup_time = time(NULL);

    log_msg_info(&logger, BeaconSender, "creating beacon_sender thread...");
    pthread_create(&beacon_sender, NULL, spawnBeaconSender, threadArgs);
    log_msg_info(&logger, BeaconSender, "created beacon_sender thread.");

    log_msg_info(&logger, CmdAgent, "creating cmd_agent thread...");
    pthread_create(&cmd_agent, NULL, spawnCmdAgent, threadArgs);
    log_msg_info(&logger, CmdAgent, "created cmd_agent thread.");

    pthread_join(beacon_sender, NULL);
    pthread_join(cmd_agent, NULL);
}