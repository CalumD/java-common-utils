package com.clumd.projects.java_common_utils.base_enhancements;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

class ObjectOutputStreamWithCustomisableHeadersTest {

    @Test
    void testObjectCreation() {
        fail("This test will fail until I figure out the TODOs in PortableSocket's Object(In/Out)putStreams");
//        assertDoesNotThrow(() -> new ObjectInputStreamWithClassLoader(
//                        new ByteArrayInputStream(new byte[]{(byte) ObjectStreamConstants.STREAM_MAGIC, (byte) ObjectStreamConstants.STREAM_VERSION}),
//                        new URLClassLoader(new URL[]{}, ObjectInputStreamWithClassLoader.class.getClassLoader())
//                )
//        );
    }

}
