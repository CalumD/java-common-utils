package com.clumd.projects.java_common_utils.base_enhancements;

import lombok.NonNull;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * This is used to point to allow the caller to provide a custom overridden set of objects to be written to the OutputStream when initialising
 */
public class ObjectOutputStreamWithCustomisableHeaders extends ObjectOutputStream {

    private final Object[] requiredStreamHeaderContent;


    /**
     * Constructor to allow callers to provide the list of objects to be written to the output stream.
     *
     * @param out                         The outputStream as normal
     * @param requiredStreamHeaderContent An optional collection of parameters which this OutputStream wrapper will write during stream
     *                                    initialisation. When no variadic arguments are provided, the default {@link ObjectOutputStream} objects
     *                                    will be written. When a `{@code (Object[]) null}` is provided as the first AND ONLY argument, NO objects
     *                                    will be written to successfully initialise. When multiple arguments are provided, each argument in
     *                                    sequence will be written to the OutputStream to successfully initialise.
     * @throws IOException Thrown as per super's specification
     */
    public ObjectOutputStreamWithCustomisableHeaders(@NonNull OutputStream out, Object... requiredStreamHeaderContent) throws IOException {
        super(out);
        this.requiredStreamHeaderContent = requiredStreamHeaderContent;
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        if (requiredStreamHeaderContent == null) {
            return;
        }
        if (requiredStreamHeaderContent.length == 0) {
            super.writeStreamHeader();
            return;
        }
        for (Object o : requiredStreamHeaderContent) {
            super.writeObject(o);
        }
    }
}
