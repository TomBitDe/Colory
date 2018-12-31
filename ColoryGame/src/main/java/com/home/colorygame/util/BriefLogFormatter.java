package com.home.colorygame.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Format a LogRecord into a brief format. The formatter creates a shorter output format for java logging. The output
 * looks like this (single line):
 * <p>
 * <code>2009-12-30 10:10:06.732 INFO [root|main]: This is the log message</code>
 * </p>
 */
public class BriefLogFormatter extends Formatter {
    private static final String LINESEP = System.getProperty("line.separator");
    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * A custom format implementation that is designed for brevity.
     *
     * @param record the content to log
     *
     * @return a String representation of the content to log
     */
    @Override
    public String format(LogRecord record) {
        String loggerName = record.getLoggerName();
        if (loggerName == null) {
            loggerName = "root";
        }
        StringBuilder output = new StringBuilder()
                .append(format.format(new Date(record.getMillis())))
                .append(" ").append(record.getLevel())
                .append(" [").append(loggerName).append('|').append(Thread.currentThread().getName()).append("]: ")
                .append(record.getMessage()).append(' ').append(LINESEP);
        return output.toString();
    }
}
