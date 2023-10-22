package com.clumd.projects.java_common_utils.base_enhancements;

import com.clumd.projects.java_common_utils.AsyncTestThread;
import com.clumd.projects.java_common_utils.NetworkingTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
                serverSend.reset();
            } catch (Exception e) {
                fail(e);
            }
        });
        serverThread.start();

        Socket client = new Socket(InetAddress.getLoopbackAddress(), NetworkingTestUtils.FIRST_SERVER_PORT);
        ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
        outputStream.writeBoolean(true);
        outputStream.flush();
        assertDoesNotThrow(() -> {
            try (ObjectInputStreamWithClassLoader ois = new ObjectInputStreamWithClassLoader(client.getInputStream(), urlClassLoader)) {
                ois.available();
            }
        });
        client.close();

        serverThread.finalise();
    }
}
