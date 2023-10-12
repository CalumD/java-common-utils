package com.clumd.projects.java_common_utils.base_enhancements;

import com.clumd.projects.java_common_utils.AsyncTestThread;
import com.clumd.projects.java_common_utils.NetworkingTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamConstants;
import java.net.*;

import static org.junit.jupiter.api.Assertions.*;

class ObjectInputStreamWithClassLoaderTest {

    private URLClassLoader urlClassLoader;

    @BeforeEach
    void setup() {
        urlClassLoader = new URLClassLoader(new URL[]{});
    }

    @Test
    void testObjectInputStreamIsCreated() throws IOException, InterruptedException {
        AsyncTestThread serverThread = new AsyncTestThread(() -> {
            try (
                    ServerSocket testServerSocket = new ServerSocket(NetworkingTestUtils.FIRST_SERVER_PORT, 0, InetAddress.getLoopbackAddress());
                    Socket serverSocket = testServerSocket.accept();
                    ObjectOutputStream serverSend = new ObjectOutputStream(serverSocket.getOutputStream());
                    ObjectInputStreamWithClassLoader serverReceive = new ObjectInputStreamWithClassLoader(serverSocket.getInputStream(), urlClassLoader)
            ) {
                assertNotNull(serverSend);
                assertNotNull(serverReceive);
                assertTrue(serverReceive.readBoolean());
            } catch (Exception e) {
                fail(e);
            }
        });
        serverThread.start();
        NetworkingTestUtils.sleep();

        Socket client = new Socket(InetAddress.getLoopbackAddress(), NetworkingTestUtils.FIRST_SERVER_PORT);
        ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
        outputStream.writeBoolean(true);
        outputStream.flush();
        assertDoesNotThrow(() -> new ObjectInputStreamWithClassLoader(client.getInputStream(), urlClassLoader));
        client.close();

        serverThread.finalise();
    }

    @Test
    void testObjectCreation() {
//        fail("This test will fail until I figure out the TODOs in PortableSocket's Object(In/Out)putStreams");
        assertDoesNotThrow(() -> new ObjectInputStreamWithClassLoader(
                        new ByteArrayInputStream(new byte[]{(byte) ObjectStreamConstants.STREAM_MAGIC, (byte) ObjectStreamConstants.STREAM_VERSION}),
                        new URLClassLoader(new URL[]{}, ObjectInputStreamWithClassLoader.class.getClassLoader())
                )
        );
    }
}
