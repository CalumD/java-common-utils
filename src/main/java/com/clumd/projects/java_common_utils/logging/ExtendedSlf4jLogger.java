package com.clumd.projects.java_common_utils.logging;

import com.clumd.projects.java_common_utils.logging.common.CustomLevel;
import org.slf4j.Marker;

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
        log(CustomLevel.TRACE, () -> String.format(format, arg));
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        log(CustomLevel.TRACE, () -> String.format(format, arg1, arg2));
    }

    @Override
    public void trace(String format, Object... arguments) {
        log(CustomLevel.TRACE, () -> String.format(format, arguments));
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
            log(CustomLevel.TRACE, markers, () -> String.format(format, arg));
        } else {
            log(CustomLevel.TRACE, () -> String.format(format, arg));
        }
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.TRACE, markers, () -> String.format(format, arg1, arg2));
        } else {
            log(CustomLevel.TRACE, () -> String.format(format, arg1, arg2));
        }
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.TRACE, markers, () -> String.format(format, argArray));
        } else {
            log(CustomLevel.TRACE, () -> String.format(format, argArray));
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
        log(CustomLevel.DEBUG, () -> String.format(format, arg));
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        log(CustomLevel.DEBUG, () -> String.format(format, arg1, arg2));
    }

    @Override
    public void debug(String format, Object... arguments) {
        log(CustomLevel.DEBUG, () -> String.format(format, arguments));
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
            log(CustomLevel.DEBUG, markers, () -> String.format(format, arg));
        } else {
            log(CustomLevel.DEBUG, () -> String.format(format, arg));
        }
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.DEBUG, markers, () -> String.format(format, arg1, arg2));
        } else {
            log(CustomLevel.DEBUG, () -> String.format(format, arg1, arg2));
        }
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.DEBUG, markers, () -> String.format(format, arguments));
        } else {
            log(CustomLevel.DEBUG, () -> String.format(format, arguments));
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
        log(CustomLevel.INFO, () -> String.format(format, arg));
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        log(CustomLevel.INFO, () -> String.format(format, arg1, arg2));
    }

    @Override
    public void info(String format, Object... arguments) {
        log(CustomLevel.INFO, () -> String.format(format, arguments));
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
            log(CustomLevel.INFO, markers, () -> String.format(format, arg));
        } else {
            log(CustomLevel.INFO, () -> String.format(format, arg));
        }
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.INFO, markers, () -> String.format(format, arg1, arg2));
        } else {
            log(CustomLevel.INFO, () -> String.format(format, arg1, arg2));
        }
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.INFO, markers, () -> String.format(format, arguments));
        } else {
            log(CustomLevel.INFO, () -> String.format(format, arguments));
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
        log(CustomLevel.WARNING, () -> String.format(format, arg));
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        log(CustomLevel.WARNING, () -> String.format(format, arg1, arg2));
    }

    @Override
    public void warn(String format, Object... arguments) {
        log(CustomLevel.WARNING, () -> String.format(format, arguments));
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
            log(CustomLevel.WARNING, markers, () -> String.format(format, arg));
        } else {
            log(CustomLevel.WARNING, () -> String.format(format, arg));
        }
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.WARNING, markers, () -> String.format(format, arg1, arg2));
        } else {
            log(CustomLevel.WARNING, () -> String.format(format, arg1, arg2));
        }
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.WARNING, markers, () -> String.format(format, arguments));
        } else {
            log(CustomLevel.WARNING, () -> String.format(format, arguments));
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
        log(CustomLevel.ERROR, () -> String.format(format, arg));
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        log(CustomLevel.ERROR, () -> String.format(format, arg1, arg2));
    }

    @Override
    public void error(String format, Object... arguments) {
        log(CustomLevel.ERROR, () -> String.format(format, arguments));
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
            log(CustomLevel.ERROR, markers, () -> String.format(format, arg));
        } else {
            log(CustomLevel.ERROR, () -> String.format(format, arg));
        }
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.ERROR, markers, () -> String.format(format, arg1, arg2));
        } else {
            log(CustomLevel.ERROR, () -> String.format(format, arg1, arg2));
        }
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        if (marker != null) {
            Set<String> markers = new HashSet<>();
            markers.add(marker.getName());
            marker.iterator().forEachRemaining(m -> markers.add(m.getName()));
            log(CustomLevel.ERROR, markers, () -> String.format(format, arguments));
        } else {
            log(CustomLevel.ERROR, () -> String.format(format, arguments));
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
