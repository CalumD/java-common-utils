package com.clumd.projects.java_common_utils.base_enhancements;

import com.clumd.projects.java_common_utils.AsyncTestThread;
import com.clumd.projects.java_common_utils.NetworkingTestUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class PortableSocketTest {

    @Test
    void test_passing_a_disconnected_socket_to_portable_socket_constructor_throws() throws IOException {
        Socket socket = new Socket();
        try {
            new PortableSocket(socket);
            fail("Expected the previous line to throw an exception.");
        } catch (UnsupportedOperationException u) {
            assertTrue(u.getMessage().contains("Invalid way to create a PortableSocket wrapper"));
            assertNotNull(u.getCause());
            assertTrue(u.getCause().getMessage().contains("You passed an already closed, or never previously connected socket"));
        }
    }

    @Test
    void test_creating_a_new_portable_socket_with_default_stream_header_behaviour() throws IOException, InterruptedException {
        AsyncTestThread serverThread = new AsyncTestThread(() -> {
            try (
                    ServerSocket testServerSocket = new ServerSocket(NetworkingTestUtils.FIRST_SERVER_PORT, 0, InetAddress.getLoopbackAddress());
                    Socket serverSocket = testServerSocket.accept();

                    PortableSocket pss = new PortableSocket(serverSocket);
            ) {
                assertNotNull(pss.getOutputStream());
                assertNotNull(pss.getInputStream());
                assertTrue(pss.getInputStream().readBoolean());
            } catch (Exception e) {
                fail(e);
            }
        });
        serverThread.start();
        NetworkingTestUtils.sleep();


        Socket client = new Socket(InetAddress.getLoopbackAddress(), NetworkingTestUtils.FIRST_SERVER_PORT);
        PortableSocket cs1 = new PortableSocket(client);

        ObjectOutputStream outputStream = cs1.getOutputStream();
        assertNotNull(outputStream);
        assertNotNull(cs1.getInputStream());

        outputStream.writeBoolean(true);
        outputStream.flush();
        cs1.close();

        serverThread.finalise();
    }

    @Test
    void testOtherThings() {
        // TODO: do more tests
        assertTrue(true);
    }
}
