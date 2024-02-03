package com.clumd.projects.java_common_utils.base_enhancements;

import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.net.URLClassLoader;

/**
 * This is used to point to a provided custom classloader as the first point of contact for loading new objects over a
 * connection.
 * It also allows the caller to provide a custom overridden set of objects expected to be read from the InputStream when initialising
 */
public class ObjectInputStreamWithClassLoader extends ObjectInputStream {

    private final URLClassLoader customLoader;

    /**
     * Custom constructor to include the classloader for the stream.
     *
     * @param in                          The inputStream as normal
     * @param customLoader                The Custom Class loader to try first for all objects received over this stream.
     * @throws IOException Thrown as per super's specification
     */
    public ObjectInputStreamWithClassLoader(@NonNull InputStream in, @NonNull URLClassLoader customLoader) throws IOException {
        super(in);
        this.customLoader = customLoader;
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
}
