#include <stdio.h>
#include <stdarg.h>
#include <stdlib.h>

#include "logger.h"


void log_msg(enum LogSrc logSrc, char* color_on, char const* fmt, va_list arg) {
        printf("%s", color_on);
        printf("%s: ", logSrc == CmdAgent ? "CmdAgent" : "BeaconSender");
        vprintf(fmt, arg);
        printf("\n \033[0m");
}

void log_msg_info(struct Logger *logger, enum LogSrc logSrc, char const* fmt, ...) {
    if (
        (logger->shouldLogBeaconSender && logSrc == BeaconSender) ||
        (logger->shouldLogCmdAgent && logSrc == CmdAgent)
    ) {
        va_list arg;
        va_start(arg, fmt);
        log_msg(logSrc, "\033[1;34m", fmt, arg);
        va_end(arg);
    }
}

void log_msg_fatal(struct Logger *logger, enum LogSrc logSrc, char const* fmt, ...) {
    va_list arg;
    va_start(arg, fmt);
    log_msg(logSrc, "\033[1m\033[31m", fmt, arg);
    perror(NULL);
    va_end(arg);
    exit(0);
}