package com.clumd.projects.java_common_utils.logging;

import com.clumd.projects.java_common_utils.logging.common.CustomLevel;
import org.slf4j.Marker;
import org.slf4j.helpers.MessageFormatter;

import java.util.HashSet;
import java.util.Set;

/**
 * A SLF4J compatible logger, with pass-through to existing {@link ExtendedLogger} methods.
 */
public class ExtendedSlf4jLogger extends ExtendedLogger implements org.slf4j.Logger {

    protected ExtendedSlf4jLogger(final String name) {
        super(name, null);
    }

    @Override
    public boolean isTraceEnabled() {
        return super.isLoggable(CustomLevel.TRACE);
    }

    @Override
    public void trace(String msg) {
        log(CustomLevel.TRACE, msg);
    }

    @Override
    public void trace(String format, Object arg) {
        log(CustomLevel.TRACE, () -> MessageFormatter.format(format, arg).getMessage());
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        log(CustomLevel.TRACE, () -> MessageFormatter.format(format, arg1, arg2).getMessage());
    }

    @Override
    public void trace(String format, Object... arguments) {
        log(CustomLevel.TRACE, () -> MessageFormatter.arrayFormat(format, arguments).getMessage());
    }

    @Override
    public void trace(String msg, Throwable t) {
        log(CustomLevel.TRACE, msg, t);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return super.isLoggable(CustomLevel.TRACE);
    }

    @Override
    public void trace(Marker marker, String msg) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.TRACE, markers, msg);
        } else {
            log(CustomLevel.TRACE, msg);
        }
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.TRACE, markers, () -> MessageFormatter.format(format, arg).getMessage());
        } else {
            log(CustomLevel.TRACE, () -> MessageFormatter.format(format, arg).getMessage());
        }
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.TRACE, markers, () -> MessageFormatter.format(format, arg1, arg2).getMessage());
        } else {
            log(CustomLevel.TRACE, () -> MessageFormatter.format(format, arg1, arg2).getMessage());
        }
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.TRACE, markers, () -> MessageFormatter.arrayFormat(format, argArray).getMessage());
        } else {
            log(CustomLevel.TRACE, () -> MessageFormatter.arrayFormat(format, argArray).getMessage());
        }
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.TRACE, markers, msg, t);
        } else {
            log(CustomLevel.TRACE, msg, t);
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return super.isLoggable(CustomLevel.DEBUG);
    }

    @Override
    public void debug(String msg) {
        log(CustomLevel.DEBUG, msg);
    }

    @Override
    public void debug(String format, Object arg) {
        log(CustomLevel.DEBUG, () -> MessageFormatter.format(format, arg).getMessage());
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        log(CustomLevel.DEBUG, () -> MessageFormatter.format(format, arg1, arg2).getMessage());
    }

    @Override
    public void debug(String format, Object... arguments) {
        log(CustomLevel.DEBUG, () -> MessageFormatter.arrayFormat(format, arguments).getMessage());
    }

    @Override
    public void debug(String msg, Throwable t) {
        log(CustomLevel.DEBUG, msg, t);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return super.isLoggable(CustomLevel.DEBUG);
    }

    @Override
    public void debug(Marker marker, String msg) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.DEBUG, markers, msg);
        } else {
            log(CustomLevel.DEBUG, msg);
        }
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.DEBUG, markers, () -> MessageFormatter.format(format, arg).getMessage());
        } else {
            log(CustomLevel.DEBUG, () -> MessageFormatter.format(format, arg).getMessage());
        }
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.DEBUG, markers, () -> MessageFormatter.format(format, arg1, arg2).getMessage());
        } else {
            log(CustomLevel.DEBUG, () -> MessageFormatter.format(format, arg1, arg2).getMessage());
        }
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.DEBUG, markers, () -> MessageFormatter.arrayFormat(format, arguments).getMessage());
        } else {
            log(CustomLevel.DEBUG, () -> MessageFormatter.arrayFormat(format, arguments).getMessage());
        }
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.DEBUG, markers, msg, t);
        } else {
            log(CustomLevel.DEBUG, msg, t);
        }
    }

    @Override
    public boolean isInfoEnabled() {
        return super.isLoggable(CustomLevel.INFO);
    }

    @Override
    public void info(String format, Object arg) {
        log(CustomLevel.INFO, () -> MessageFormatter.format(format, arg).getMessage());
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        log(CustomLevel.INFO, () -> MessageFormatter.format(format, arg1, arg2).getMessage());
    }

    @Override
    public void info(String format, Object... arguments) {
        log(CustomLevel.INFO, () -> MessageFormatter.arrayFormat(format, arguments).getMessage());
    }

    @Override
    public void info(String msg, Throwable t) {
        log(CustomLevel.INFO, msg, t);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return super.isLoggable(CustomLevel.INFO);
    }

    @Override
    public void info(Marker marker, String msg) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.INFO, markers, msg);
        } else {
            log(CustomLevel.INFO, msg);
        }
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.INFO, markers, () -> MessageFormatter.format(format, arg).getMessage());
        } else {
            log(CustomLevel.INFO, () -> MessageFormatter.format(format, arg).getMessage());
        }
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.INFO, markers, () -> MessageFormatter.format(format, arg1, arg2).getMessage());
        } else {
            log(CustomLevel.INFO, () -> MessageFormatter.format(format, arg1, arg2).getMessage());
        }
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.INFO, markers, () -> MessageFormatter.arrayFormat(format, arguments).getMessage());
        } else {
            log(CustomLevel.INFO, () -> MessageFormatter.arrayFormat(format, arguments).getMessage());
        }
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.INFO, markers, msg, t);
        } else {
            log(CustomLevel.INFO, msg, t);
        }
    }

    @Override
    public boolean isWarnEnabled() {
        return super.isLoggable(CustomLevel.WARNING);
    }

    @Override
    public void warn(String msg) {
        log(CustomLevel.WARNING, msg);
    }

    @Override
    public void warn(String format, Object arg) {
        log(CustomLevel.WARNING, () -> MessageFormatter.format(format, arg).getMessage());
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        log(CustomLevel.WARNING, () -> MessageFormatter.format(format, arg1, arg2).getMessage());
    }

    @Override
    public void warn(String format, Object... arguments) {
        log(CustomLevel.WARNING, () -> MessageFormatter.arrayFormat(format, arguments).getMessage());
    }

    @Override
    public void warn(String msg, Throwable t) {
        log(CustomLevel.WARNING, msg, t);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return super.isLoggable(CustomLevel.WARNING);
    }

    @Override
    public void warn(Marker marker, String msg) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.WARNING, markers, msg);
        } else {
            log(CustomLevel.WARNING, msg);
        }
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.WARNING, markers, () -> MessageFormatter.format(format, arg).getMessage());
        } else {
            log(CustomLevel.WARNING, () -> MessageFormatter.format(format, arg).getMessage());
        }
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.WARNING, markers, () -> MessageFormatter.format(format, arg1, arg2).getMessage());
        } else {
            log(CustomLevel.WARNING, () -> MessageFormatter.format(format, arg1, arg2).getMessage());
        }
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.WARNING, markers, () -> MessageFormatter.arrayFormat(format, arguments).getMessage());
        } else {
            log(CustomLevel.WARNING, () -> MessageFormatter.arrayFormat(format, arguments).getMessage());
        }
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.WARNING, markers, msg, t);
        } else {
            log(CustomLevel.WARNING, msg, t);
        }
    }

    @Override
    public boolean isErrorEnabled() {
        return super.isLoggable(CustomLevel.ERROR);
    }

    @Override
    public void error(String msg) {
        log(CustomLevel.ERROR, msg);
    }

    @Override
    public void error(String format, Object arg) {
        log(CustomLevel.ERROR, () -> MessageFormatter.format(format, arg).getMessage());
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        log(CustomLevel.ERROR, () -> MessageFormatter.format(format, arg1, arg2).getMessage());
    }

    @Override
    public void error(String format, Object... arguments) {
        log(CustomLevel.ERROR, () -> MessageFormatter.arrayFormat(format, arguments).getMessage());
    }

    @Override
    public void error(String msg, Throwable t) {
        log(CustomLevel.ERROR, msg, t);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return super.isLoggable(CustomLevel.ERROR);
    }

    @Override
    public void error(Marker marker, String msg) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.ERROR, markers, msg);
        } else {
            log(CustomLevel.ERROR, msg);
        }
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.ERROR, markers, () -> MessageFormatter.format(format, arg).getMessage());
        } else {
            log(CustomLevel.ERROR, () -> MessageFormatter.format(format, arg).getMessage());
        }
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.ERROR, markers, () -> MessageFormatter.format(format, arg1, arg2).getMessage());
        } else {
            log(CustomLevel.ERROR, () -> MessageFormatter.format(format, arg1, arg2).getMessage());
        }
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.ERROR, markers, () -> MessageFormatter.arrayFormat(format, arguments).getMessage());
        } else {
            log(CustomLevel.ERROR, () -> MessageFormatter.arrayFormat(format, arguments).getMessage());
        }
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.ERROR, markers, msg, t);
        } else {
            log(CustomLevel.ERROR, msg, t);
        }
    }
}
