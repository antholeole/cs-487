#ifndef LOGGER_H
#define LOGGER_H

enum LogSrc {
    BeaconSender,
    CmdAgent,
};

typedef struct Logger {
    int shouldLogBeaconSender;
    int shouldLogCmdAgent;
} logger;

void log_msg_info(struct Logger *logger, enum LogSrc src, char const* fmt, ...);
void log_msg_fatal(struct Logger *logger, enum LogSrc src, char const* fmt, ...);

#endif