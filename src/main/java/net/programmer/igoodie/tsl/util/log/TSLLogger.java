package net.programmer.igoodie.tsl.util.log;

import java.io.PrintStream;

public final class TSLLogger {

    private PrintStream out = System.out;
    private boolean timestampEnabled = true;
    private TSLLogLevel logLevel = TSLLogLevel.ALL;

    public TSLLogger enableTimestamp(boolean enabled) {
        this.timestampEnabled = enabled;
        return this;
    }

    public TSLLogger setLogLevel(TSLLogLevel level) {
        this.logLevel = level;
        return this;
    }

    public TSLLogger setOutputStream(PrintStream out) {
        this.out = out;
        return this;
    }

    public void log(TSLLogLevel level, String prefix, String message, Object... args) {
        if (!this.logLevel.shouldLog(level))
            return;

        String printThis = args.length != 0
                ? String.format(message, args)
                : message;

        // Timestamp
        if (timestampEnabled)
            out.printf("[%d] ", System.currentTimeMillis());

        // Log prefix
        out.printf("[%s] ", prefix);

        // Message!
        out.println(printThis);
    }

    public void trace(String message, Object... args) {
        log(TSLLogLevel.TRACE, "TRACE", message, args);
    }

    public void debug(String message, Object... args) {
        log(TSLLogLevel.DEBUG, "DEBUG", message, args);
    }

    public void info(String message, Object... args) {
        log(TSLLogLevel.INFO, "INFO", message, args);
    }

    public void warn(String message, Object... args) {
        log(TSLLogLevel.WARN, "WARN", message, args);
    }

    public void error(String message, Object... args) {
        log(TSLLogLevel.ERROR, "ERROR", message, args);
    }

    public void fatal(String message, Object... args) {
        log(TSLLogLevel.FATAL, "FATAL", message, args);
    }

}
