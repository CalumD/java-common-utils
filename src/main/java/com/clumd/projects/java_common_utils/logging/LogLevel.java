package com.clumd.projects.java_common_utils.logging;

import java.io.Serializable;

public interface LogLevel extends Serializable {

    String getLevelName();
    int getPriority();
    String getLevelFormat();
}
