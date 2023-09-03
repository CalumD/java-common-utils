package com.clumd.projects.java_common_utils.base_enhancements;

import lombok.NonNull;

import java.io.*;
import java.net.URLClassLoader;

/**
 * This is used to point to a provided custom classloader as the first point of contact for loading new objects over a
 * connection.
 * It also allows the caller to provide a custom overridden set of objects expected to be read from the InputStream when initialising
 */
public class ObjectInputStreamWithClassLoaderAndHeaders extends ObjectInputStream {

    private final URLClassLoader customLoader;
    private final Object[] requiredStreamHeaderContent;


    /**
     * Custom constructor to include the classloader for the stream.
     *
     * @param in                          The inputStream as normal
     * @param customLoader                The Custom Class loader to try first for all objects received over this stream.
     * @param requiredStreamHeaderContent An optional collection of parameters which this InputStream wrapper expects to read on stream
     *                                    initialisation. When no variadic arguments are provided, the default {@link ObjectInputStream} objects
     *                                    will be required. When a `{@code (Object[]) null}` is provided as the first AND ONLY argument, NO objects
     *                                    will be required to be present in the InputStream to successfully initialise. When multiple arguments are
     *                                    provided, each argument in sequence will be required to be read from the InputStream to successfully
     *                                    initialise.
     * @throws IOException Thrown as per super's specification
     */
    public ObjectInputStreamWithClassLoaderAndHeaders(@NonNull InputStream in, @NonNull URLClassLoader customLoader, Object... requiredStreamHeaderContent) throws IOException {
        super(in);
        this.customLoader = customLoader;
        this.requiredStreamHeaderContent = requiredStreamHeaderContent;
    }

    /**
     * Overridden to point to the custom classloader first.
     *
     * @param deserializedClassDescription As normal
     * @return As normal
     * @throws ClassNotFoundException as normal.
     */
    @Override
    protected Class<?> resolveClass(ObjectStreamClass deserializedClassDescription) throws ClassNotFoundException {
        return Class.forName(deserializedClassDescription.getName(), true, customLoader);
    }

    @Override
    protected void readStreamHeader() throws IOException {
        if (requiredStreamHeaderContent == null) {
            return;
        }
        if (requiredStreamHeaderContent.length == 0) {
            super.readStreamHeader();
            return;
        }
        Object readObject;
        for (Object o : requiredStreamHeaderContent) {
            try {
                readObject = super.readObject();
            } catch (ClassNotFoundException e) {
                throw new StreamCorruptedException("Failed to initialise ObjectInputStream with custom header requirements. " +
                        "Unknown class received. " + e.getMessage());
            }
            if (o == null) {
                if (readObject != null) {
                    throw new StreamCorruptedException("Expected to read a null in the stream, but got something else instead " +
                            "{" + readObject.getClass().getCanonicalName() + "}");
                }
                continue;
            }
            if (!o.equals(readObject)) {
                throw new StreamCorruptedException("Expected to read a " +
                        "{" + readObject.getClass().getCanonicalName() + "} from the stream header, but got a non-matching " +
                        "{" + readObject.getClass().getCanonicalName() + "} instead.");
            }
        }
    }
}
