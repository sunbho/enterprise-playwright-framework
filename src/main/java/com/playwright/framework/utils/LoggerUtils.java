package com.playwright.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Creates SLF4J loggers for framework components.
 */
public final class LoggerUtils {

    private LoggerUtils() {
        throw new IllegalStateException("LoggerUtils must not be instantiated");
    }

    /**
     * Returns a logger named for the supplied class.
     *
     * @param owner logger owner
     * @return SLF4J logger
     */
    public static Logger getLogger(Class<?> owner) {
        return LoggerFactory.getLogger(Objects.requireNonNull(owner, "owner must not be null"));
    }
}
