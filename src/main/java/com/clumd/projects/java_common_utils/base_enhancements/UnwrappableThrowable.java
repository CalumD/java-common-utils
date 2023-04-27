package com.clumd.projects.java_common_utils.base_enhancements;

import java.io.Serializable;
import java.util.List;

public interface UnwrappableThrowable extends Serializable {

    default String unwrapReasons() {
        return unwrapReasons(false);
    }

    default String unwrapReasons(final boolean includeTrace) {
        return String.join("\n", unwrapReasonsIntoList(includeTrace));
    }

    default List<String> unwrapReasonsIntoList() {
        return unwrapReasonsIntoList(false);
    }

    List<String> unwrapReasonsIntoList(final boolean includeTrace);
}
