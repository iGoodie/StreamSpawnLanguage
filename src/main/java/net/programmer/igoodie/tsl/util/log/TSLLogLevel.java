package net.programmer.igoodie.tsl.util.log;

public enum TSLLogLevel {

    OFF(0),
    FATAL(100),
    ERROR(200),
    WARN(300),
    INFO(400),
    DEBUG(500),
    TRACE(600),
    ALL(Integer.MAX_VALUE);

    int level;

    TSLLogLevel(int level) {
        this.level = level;
    }

    public boolean shouldLog(TSLLogLevel level) {
        return shouldLog(level.level);
    }

    public boolean shouldLog(int level) {
        return this.level >= level;
    }

}
