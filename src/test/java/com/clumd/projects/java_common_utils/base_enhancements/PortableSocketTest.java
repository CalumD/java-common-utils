package com.clumd.projects.java_common_utils.base_enhancements;

import com.clumd.projects.java_common_utils.AsyncTestThread;
import com.clumd.projects.java_common_utils.NetworkingTestUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
    void test_creating_a_new_portable_socket_with_default_stream_header_behaviour_using_other_portable_socket() throws IOException, InterruptedException {
        AsyncTestThread serverThread = new AsyncTestThread(() -> {
            try (
                    ServerSocket testServerSocket = new ServerSocket(NetworkingTestUtils.FIRST_SERVER_PORT, 0, InetAddress.getLoopbackAddress());
                    Socket serverSocket = testServerSocket.accept();

                    PortableSocket pss = new PortableSocket(serverSocket);
            ) {
                assertNotNull(pss.getOutputStream());
                assertNotNull(pss.getInputStream());

                assertTrue(pss.getInputStream().readBoolean());
                pss.getOutputStream().writeBoolean(false);
                pss.getOutputStream().flush();
            } catch (Exception e) {
                fail(e);
            }
        });
        serverThread.start();

        Socket client = new Socket(InetAddress.getLoopbackAddress(), NetworkingTestUtils.FIRST_SERVER_PORT);
        PortableSocket cs1 = new PortableSocket(client);

        ObjectOutputStream clientOutputStream = cs1.getOutputStream();
        assertNotNull(clientOutputStream);
        ObjectInputStream clientInputStream = cs1.getInputStream();
        assertNotNull(clientInputStream);

        clientOutputStream.writeBoolean(true);
        clientOutputStream.flush();
        assertFalse(clientInputStream.readBoolean());
        cs1.close();

        serverThread.finalise();
    }

    @Test
    void test_creating_a_new_portable_socket_with_no_stream_header_requirements_using_socket_constructor() throws IOException, InterruptedException {
        AsyncTestThread serverThread = new AsyncTestThread(() -> {
            try (
                    ServerSocket testServerSocket = new ServerSocket(NetworkingTestUtils.FIRST_SERVER_PORT, 0, InetAddress.getLoopbackAddress());
                    Socket serverSocket = testServerSocket.accept();

                    PortableSocket pss = new PortableSocket(serverSocket, (Object[]) null);
            ) {
                assertNotNull(pss.getOutputStream());
                assertNotNull(pss.getInputStream());

                assertTrue(pss.getInputStream().readBoolean());
                pss.getOutputStream().writeBoolean(false);
                pss.getOutputStream().flush();
            } catch (Exception e) {
                fail(e);
            }
        });
        serverThread.start();

        Socket client = new Socket(InetAddress.getLoopbackAddress(), NetworkingTestUtils.FIRST_SERVER_PORT);
        PortableSocket cs1 = new PortableSocket(client, (Object[]) null);

        ObjectOutputStream clientOutputStream = cs1.getOutputStream();
        assertNotNull(clientOutputStream);
        ObjectInputStream clientInputStream = cs1.getInputStream();
        assertNotNull(clientInputStream);

        clientOutputStream.writeBoolean(true);
        clientOutputStream.flush();
        assertFalse(clientInputStream.readBoolean());
        cs1.close();

        serverThread.finalise();
    }

    @Test
    void test_creating_a_new_portable_socket_with_no_stream_header_requirements_using_hostname_constructor() throws IOException, InterruptedException {
        AsyncTestThread serverThread = new AsyncTestThread(() -> {
            try (
                    ServerSocket testServerSocket = new ServerSocket(NetworkingTestUtils.FIRST_SERVER_PORT, 0, InetAddress.getLoopbackAddress());
                    Socket serverSocket = testServerSocket.accept();

                    PortableSocket pss = new PortableSocket(serverSocket, (Object[]) null);
            ) {
                assertNotNull(pss.getOutputStream());
                assertNotNull(pss.getInputStream());

                assertTrue(pss.getInputStream().readBoolean());
                pss.getOutputStream().writeBoolean(false);
                pss.getOutputStream().flush();
            } catch (Exception e) {
                fail(e);
            }
        });
        serverThread.start();

        PortableSocket cs1 = new PortableSocket("127.0.0.1", NetworkingTestUtils.FIRST_SERVER_PORT, (Object[]) null);

        ObjectOutputStream clientOutputStream = cs1.getOutputStream();
        assertNotNull(clientOutputStream);
        ObjectInputStream clientInputStream = cs1.getInputStream();
        assertNotNull(clientInputStream);

        clientOutputStream.writeBoolean(true);
        clientOutputStream.flush();
        assertFalse(clientInputStream.readBoolean());
        cs1.close();

        serverThread.finalise();
    }

    @Test
    void test_creating_a_new_portable_socket_with_custom_stream_header_requirements() throws IOException, InterruptedException {
        AsyncTestThread serverThread = new AsyncTestThread(() -> {
            try (
                    ServerSocket testServerSocket = new ServerSocket(NetworkingTestUtils.FIRST_SERVER_PORT, 0, InetAddress.getLoopbackAddress());
                    Socket serverSocket = testServerSocket.accept();

                    PortableSocket pss = new PortableSocket(serverSocket, 1, "scooby doob o tron", null, false, 7357);
            ) {
                assertNotNull(pss.getOutputStream());
                assertNotNull(pss.getInputStream());

                assertTrue(pss.getInputStream().readBoolean());
                pss.getOutputStream().writeBoolean(false);
                pss.getOutputStream().flush();
            } catch (Exception e) {
                fail(e);
            }
        });
        serverThread.start();

        Socket client = new Socket(InetAddress.getLoopbackAddress(), NetworkingTestUtils.FIRST_SERVER_PORT);
        PortableSocket cs1 = new PortableSocket(client, 1, "scooby doob o tron", null, false, 7357);

        ObjectOutputStream clientOutputStream = cs1.getOutputStream();
        assertNotNull(clientOutputStream);
        ObjectInputStream clientInputStream = cs1.getInputStream();
        assertNotNull(clientInputStream);

        clientOutputStream.writeBoolean(true);
        clientOutputStream.flush();
        assertFalse(clientInputStream.readBoolean());
        cs1.close();

        serverThread.finalise();
    }

    @Test
    void test_creating_a_new_portable_socket_with_custom_stream_header_requirements_but_does_not_match_remote_due_to_nulls() throws IOException, InterruptedException {
        AsyncTestThread serverThread = new AsyncTestThread(() -> {
            try (
                    ServerSocket testServerSocket = new ServerSocket(NetworkingTestUtils.FIRST_SERVER_PORT, 0, InetAddress.getLoopbackAddress());
                    Socket serverSocket = testServerSocket.accept();

                    PortableSocket pss = new PortableSocket(serverSocket, null, false, 7357);
            ) {
                assertNotNull(pss.getInputStream());
            } catch (Exception e) {
                assertTrue(e.getMessage().contains("Failed to properly initialise a PortableSocket's input stream"));
                assertNotNull(e.getCause());
                assertTrue(e.getCause().getMessage()
                        .contains("Expected to read a null in the stream header, but got something else instead: {java.lang.Integer}")
                );
            }
        });
        serverThread.start();

        Socket client = new Socket(InetAddress.getLoopbackAddress(), NetworkingTestUtils.FIRST_SERVER_PORT);
        PortableSocket cs1 = new PortableSocket(client, 1, false, 7357);

        ObjectOutputStream clientOutputStream = cs1.getOutputStream();
        assertNotNull(clientOutputStream);
        clientOutputStream.writeBoolean(true);
        clientOutputStream.flush();

        try {
            cs1.getInputStream();
            fail("Expected the previous line to throw an exception.");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Failed to properly initialise a PortableSocket's input stream"));
            assertNotNull(e.getCause());
            assertTrue(e.getCause().getMessage()
                    .contains("Expected to read a {java.lang.Integer} from the stream header, but got a non-matching {null} instead.")
            );
        }
        cs1.close();

        serverThread.finalise();
    }

    @Test
    void test_deny_double_initialisation_of_an_input_stream() throws IOException, InterruptedException {
        AsyncTestThread serverThread = new AsyncTestThread(() -> {
            try (
                    ServerSocket testServerSocket = new ServerSocket(NetworkingTestUtils.FIRST_SERVER_PORT, 0, InetAddress.getLoopbackAddress());
                    Socket serverSocket = testServerSocket.accept();

                    PortableSocket pss = new PortableSocket(serverSocket);
            ) {
                // coverage only
                assertNotNull(pss.getSocket());
                NetworkingTestUtils.sleep();
            } catch (Exception e) {
                fail(e);
            }
        });
        serverThread.start();

        try (
                Socket client = new Socket(InetAddress.getLoopbackAddress(), NetworkingTestUtils.FIRST_SERVER_PORT);
                PortableSocket cs1 = new PortableSocket(client);
        ) {
            // Call get input stream once, which will give a default initialisation
            cs1.getInputStream();
            // This second call should now fail since we have already initialised the stream.
            cs1.initialiseInputStreamWithComponentLoader(new URLClassLoader(new URL[]{}));
            fail("Expected the line above to throw.");
        } catch (SocketException e) {
            assertTrue(e.getMessage().contains("Input Stream already initialised, cannot be re-initialised"));
        }

        serverThread.finalise();
    }

    @Test
    void test_setting_socket_timeouts() throws IOException, InterruptedException {
        AsyncTestThread serverThread = new AsyncTestThread(() -> {
            try (
                    ServerSocket testServerSocket = new ServerSocket(NetworkingTestUtils.FIRST_SERVER_PORT, 0, InetAddress.getLoopbackAddress());
                    Socket serverSocket = testServerSocket.accept();
                    PortableSocket pss = new PortableSocket(serverSocket);
            ) {
                NetworkingTestUtils.sleep();
                NetworkingTestUtils.sleep();
            } catch (Exception e) {
                fail(e);
            }
        });
        serverThread.start();

        try (
                Socket client = new Socket(InetAddress.getLoopbackAddress(), NetworkingTestUtils.FIRST_SERVER_PORT);
                PortableSocket cs1 = new PortableSocket(client);
        ) {
            assertEquals(3000, cs1.getSocket().getSoTimeout(), 0);

            assertEquals(3000, cs1.setFastTimeout(), 0);
            assertEquals(1000, cs1.getSocket().getSoTimeout(), 0);

            assertEquals(1000, cs1.setMediumTimeout(), 0);
            assertEquals(3000, cs1.getSocket().getSoTimeout(), 0);

            assertEquals(3000, cs1.setLongTimeout(), 0);
            assertEquals(10000, cs1.getSocket().getSoTimeout(), 0);

            assertEquals(10000, cs1.setOneSecondTimeout(), 0);
            assertEquals(1000, cs1.getSocket().getSoTimeout(), 0);

            assertEquals(1000, cs1.setTwoSecondTimeout(), 0);
            assertEquals(2000, cs1.getSocket().getSoTimeout(), 0);

            assertEquals(2000, cs1.setThreeSecondTimeout(), 0);
            assertEquals(3000, cs1.getSocket().getSoTimeout(), 0);

            assertEquals(3000, cs1.setFiveSecondTimeout(), 0);
            assertEquals(5000, cs1.getSocket().getSoTimeout(), 0);

            assertEquals(5000, cs1.setTenSecondTimeout(), 0);
            assertEquals(10000, cs1.getSocket().getSoTimeout(), 0);

            assertEquals(10000, cs1.setThirtySecondTimeout(), 0);
            assertEquals(30000, cs1.getSocket().getSoTimeout(), 0);

            assertEquals(30000, cs1.setCustomTimeoutInMs(12345), 0);
            assertEquals(12345, cs1.getSocket().getSoTimeout(), 0);

            assertEquals(12345, cs1.setNoTimeout(), 0);
            assertEquals(0, cs1.getSocket().getSoTimeout(), 0);

        } catch (SocketException e) {
            fail(e);
        }

        serverThread.finalise();
    }

    @Test
    void test_reachable_for_local_hostnames() throws IOException {
        assertTrue(PortableSocket.portableIsReachable("localhost", 0));
        assertTrue(PortableSocket.portableIsReachable("LOCALHOST", 0));
        assertTrue(PortableSocket.portableIsReachable("127.0.0.1", 0));
        assertTrue(PortableSocket.portableIsReachable("::1", 0));
        assertTrue(PortableSocket.portableIsReachable("0:0:0:0:0:0:0:0", 0));
        assertTrue(PortableSocket.portableIsReachable("0:0:0:0:0:0:0:1", 0));
        assertTrue(PortableSocket.portableIsReachable(InetAddress.getLocalHost().getHostName(), 0));
    }

    @Test
    void test_reachable_but_deny_due_to_invalid_name() throws IOException {
        try {
            PortableSocket.portableIsReachable("this is not a valid hostname", 0);
            fail("Expected the previous line to throw");
        } catch (UnknownHostException e) {
            if (!e.getMessage().contains("No such host is known") && !e.getMessage().contains("this is not a valid hostname")) {
                fail(e);
            }
        }
        try {
            PortableSocket.portableIsReachable("https://noHostnameLmao.co.uk/", 0);
            fail("Expected the previous line to throw");
        } catch (UnknownHostException e) {
            if (!e.getMessage().contains("No such host is known") && !e.getMessage().contains("https://noHostnameLmao.co.uk/")) {
                fail(e);
            }
        }
        try {
            PortableSocket.portableIsReachable("600.355.355.30", 0);
            fail("Expected the previous line to throw");
        } catch (UnknownHostException e) {
            if (!e.getMessage().contains("No such host is known") && !e.getMessage().contains("600.355.355.30")) {
                fail(e);
            }
        }
    }

    @Test
    void test_reachable_but_deny_due_to_not_local_or_reachable() {
        try {
            PortableSocket.portableIsReachable("0.0.0.1", 0);
            fail("Expected the previous line to throw");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Failed to initiate a connection to: {0.0.0.1:0} host is unreachable"));
        }
    }

    /**
     * This test uses a distinct method of attempting connectivity first, so that in an environment where you are trying to test with legitimately
     * no internet connectivity - the tests shouldn't fail due to the particular host being used in the test not being reachable.
     * <p></p>
     * Ultimately, it does cause this to be a rather weak test, however it would still be perfectly reasonable to expect this application to function
     * with no outside network access; but I can't code in an external address for checking in those circumstances without knowing the internal
     * structure of the networking trying to pass the test.
     */
    @Test
    void test_reachable_success() throws IOException {
        try {
            URL url = new URI("https://www.example.com/").toURL();
            URLConnection connection = url.openConnection();
            connection.connect();
        } catch (URISyntaxException | MalformedURLException e) {
            fail(e);
        } catch (IOException e) {
            System.err.println("Only passing this test on the assumption that there is actually no current internet connectivity," +
                    " so it feels kinda bad to fail on that behalf.\n" +
                    "If I can find good proof that there are reasonable circumstances where this test would always fail too, " +
                    " (other than through connectivity or the host is temporarily down) then I will need to address this test again.");
            return;
        }

        assertTrue(PortableSocket.portableIsReachable("www.example.com", 0));
    }
}
