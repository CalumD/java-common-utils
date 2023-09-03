package com.clumd.projects.java_common_utils.base_enhancements;

import lombok.Getter;
import lombok.NonNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Set;

/**
 * This {@link java.net.Socket} wrapper serves to accumulate some convenience methods for dealing with sockets, such as setting up ObjectStreams,
 * setting timeouts and preparing customised ClassLoaders for interpreting incoming objects.
 * <p>
 * One other extremely useful feature it provides is a PORTABLE method of determining a connection's reachability.
 * <p>
 * As per the
 * <a href="https://download.java.net/java/early_access/jdk21/docs/api/java.base/java/net/InetAddress.html#isReachable(int)">Java documentation</a>
 * and countless stackoverflow posts, the baked in {@link InetAddress#isReachable(int)} will only make best-effort to perform a regular ICMP PING
 * request. However, certain operating systems may only allow these calls if the JVM is running with ROOT/ADMIN privileges. This implementation
 * seeks to improve this, by going a few steps further to determine reachability such as actually trying to construct a Socket to the peer on a
 * known, provided, port.
 */
public class PortableSocket implements AutoCloseable {

    private static final int ONE_SECOND_IN_MS = 1000;
    private static final int TWO_SECONDS_IN_MS = 2000;
    private static final int THREE_SECONDS_IN_MS = 3000;
    private static final int FIVE_SECONDS_IN_MS = 5000;
    private static final int TEN_SECONDS_IN_MS = 10000;
    private static final int NO_TIMEOUT = 0;
    private static final Set<String> LOCALHOST_NAMES = Set.of(
            "localhost",
            "LOCALHOST",
            "127.0.0.1",
            "::1",
            "0:0:0:0:0:0:0:0",
            "0:0:0:0:0:0:0:1"
    );

    @Getter
    private final Socket socket;

    private final Object[] requiredSocketStreamHeaderContent;

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;


    public PortableSocket(@NonNull final Socket socket, Object... requiredSocketStreamHeaderContent) throws IOException {
        if (socket.isClosed() || socket.getRemoteSocketAddress() == null) {
            throw new UnsupportedOperationException(
                    "Invalid way to create a PortableSocket wrapper.",
                    new IllegalArgumentException("You passed an already closed, "
                            + "or never previously connected socket reference in the constructor to PortableSocket. "
                            + "To create a new connection please use the constructor with hostname and port fields.")
            );
        }
        this.socket = socket;
        this.requiredSocketStreamHeaderContent = requiredSocketStreamHeaderContent;
        setMediumTimeout();
        this.outputStream = getOutputStream();
    }

    public PortableSocket(@NonNull final String hostname, final int port, Object... requiredSocketStreamHeaderContent) throws IOException {
        socket = new Socket();
        this.requiredSocketStreamHeaderContent = requiredSocketStreamHeaderContent;
        socket.connect(new InetSocketAddress(hostname, port), THREE_SECONDS_IN_MS);
        setMediumTimeout();
        this.outputStream = getOutputStream();
    }

    public static boolean portableIsReachable(@NonNull final String hostname, final int port) throws IOException {

        if (isLocalHostname(hostname)) {
            return true;
        }

        try (Socket connectivityTestSocket = new Socket()) {

            InetAddress inetAddr = InetAddress.getByName(hostname);
            if (inetAddr.isReachable(THREE_SECONDS_IN_MS)) {
                return true;
            }

            InetSocketAddress inetSocketAddr = new InetSocketAddress(inetAddr, port);
            connectivityTestSocket.connect(inetSocketAddr, THREE_SECONDS_IN_MS);

            return true;
        } catch (IOException e) {
            throw new IOException("Failed to initiate a connection to: {" + hostname + ":" + port + "} host is unreachable.", e);
        }
    }

    public static boolean isLocalHostname(@NonNull final String hostname) throws IOException {

        // Quick and dirty check without having to do any network look-ups
        if (LOCALHOST_NAMES.contains(hostname)) {
            return true;
        }

        InetAddress inetAddress = InetAddress.getByName(hostname);
        return (inetAddress.isAnyLocalAddress() || inetAddress.isLoopbackAddress() || InetAddress.getLocalHost().equals(inetAddress));
    }

    public ObjectInputStream initialiseInputStreamWithComponentLoader(@NonNull final URLClassLoader componentLoaderForInputStream) throws IOException {
        if (inputStream != null) {
            throw new SocketException("Input Stream already initialised, cannot be re-initialised.");
        }

        try {
            inputStream = new ObjectInputStreamWithClassLoaderAndHeaders(
                    this.socket.getInputStream(),
                    componentLoaderForInputStream,
                    requiredSocketStreamHeaderContent
            );
        } catch (final IOException e) {
            throw new IOException("Failed to properly initialise a PortableSocket's input stream", e);
        }

        return getInputStream();
    }

    public ObjectOutputStream getOutputStream() throws IOException {
        if (outputStream == null) {
            outputStream = new ObjectOutputStreamWithCustomisableHeaders(
                    socket.getOutputStream(),
                    requiredSocketStreamHeaderContent
            );
        }

        return outputStream;
    }

    public ObjectInputStream getInputStream() throws IOException {
        if (inputStream == null) {
            return initialiseInputStreamWithComponentLoader(new URLClassLoader(new URL[]{}));
        }

        return inputStream;
    }

    @Override
    public void close() {
        try {
            if (!socket.isClosed()) {
                outputStream.flush();
                socket.close();
            }
        } catch (IOException e) {
            // ignore as going to terminate anyway.
        }
    }

    public int setFastTimeout() throws SocketException {
        return setOneSecondTimeout();
    }

    public int setMediumTimeout() throws SocketException {
        return setThreeSecondTimeout();
    }

    public int setLongTimeout() throws SocketException {
        return setTenSecondTimeout();
    }

    public int setOneSecondTimeout() throws SocketException {
        int previousTimeout = socket.getSoTimeout();
        socket.setSoTimeout(ONE_SECOND_IN_MS);
        return previousTimeout;
    }

    public int setTwoSecondTimeout() throws SocketException {
        int previousTimeout = socket.getSoTimeout();
        socket.setSoTimeout(TWO_SECONDS_IN_MS);
        return previousTimeout;
    }

    public int setThreeSecondTimeout() throws SocketException {
        int previousTimeout = socket.getSoTimeout();
        socket.setSoTimeout(THREE_SECONDS_IN_MS);
        return previousTimeout;
    }

    public int setFiveSecondTimeout() throws SocketException {
        int previousTimeout = socket.getSoTimeout();
        socket.setSoTimeout(FIVE_SECONDS_IN_MS);
        return previousTimeout;
    }

    public int setTenSecondTimeout() throws SocketException {
        int previousTimeout = socket.getSoTimeout();
        socket.setSoTimeout(TEN_SECONDS_IN_MS);
        return previousTimeout;
    }

    public int setNoTimeout() throws SocketException {
        int previousTimeout = socket.getSoTimeout();
        socket.setSoTimeout(NO_TIMEOUT);
        return previousTimeout;
    }

    public int setCustomTimeoutInMs(final int timeoutInMilliseconds) throws SocketException {
        int previousTimeout = socket.getSoTimeout();
        socket.setSoTimeout(timeoutInMilliseconds);
        return previousTimeout;
    }
}
