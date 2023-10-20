package com.clumd.projects.java_common_utils.base_enhancements;

import lombok.Getter;
import lombok.NonNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;

/**
 * This {@link java.net.Socket} wrapper serves to accumulate some convenience methods for dealing with sockets, such as setting up ObjectStreams,
 * setting timeouts and preparing customised ClassLoaders for interpreting incoming objects. It is also called 'Portable' as THIS is the object
 * reference which should be passed around to represent a socket endpoint, rather than the socket itself and create multiple input/output streams on.
 * <p>
 * One other extremely useful feature it provides is a PORTABLE method of determining a connection's reachability.
 * <p>
 * As per the
 * <a href="https://download.java.net/java/early_access/jdk21/docs/api/java.base/java/net/InetAddress.html#isReachable(int)">Java documentation</a>
 * and countless stackoverflow posts, the baked in {@link InetAddress#isReachable(int)} will only make best-effort to perform a regular ICMP PING
 * request. However, certain operating systems may only allow these calls if the JVM is running with ROOT/ADMIN privileges. This implementation
 * ({@link #portableIsReachable(String, int)})
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


    /**
     * This constructor accepts a *connected* Socket, and potentially, a collection of objects which will be written and expected to be read as a
     * stream header. This allows both sides of a PortableSocket to match each other reliably.
     * This constructor will create an OutputStream for this socket, will set a Medium timeout, but will NOT immediately create an InputStream.
     * This is to allow a caller to potentially provide a custom URL ClassLoader through
     * {@link #initialiseInputStreamWithComponentLoader(URLClassLoader)}.
     *
     * @param socket                            The already connected low level Socket. If a socket is provided but is not connected, then you can't
     *                                          use a portable socket yet as, to cope with this, involves having to then keep track of all the is
     *                                          initialised and connected etc. Basically I'm just a bit lazy, but I don't think it makes sense for
     *                                          this class.
     * @param requiredSocketStreamHeaderContent Variadic args for what to write down the socket on connection, and expect to read from the socket
     *                                          on connection.
     *                                          Passing nothing and treating this constructor as if this variadic did not exist, will leave the
     *                                          DEFAULT (In/Out)putStream header behaviour in place.
     *                                          Passing a type-casted {@code (Object[]) null}, will disable any read/write operations within the
     *                                          streams initialisations.
     *                                          Passing any other variadic data, will write those objects in order and expect to read the same from
     *                                          the other side on initialisation.
     * @throws IOException This can be thrown if there was an issue creating the output streams, or defaulting the socket operation timeouts.
     */
    public PortableSocket(@NonNull final Socket socket, final Serializable... requiredSocketStreamHeaderContent) throws IOException {
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

    /**
     * This constructor accepts a hostname and port which we would like to connect to, and potentially, a collection of objects which will be written
     * and expected to be read as a stream header. This allows both sides of a PortableSocket to match each other reliably.
     * If this constructor fails to produce a successful connection to the hostname and port provided and exception will be thrown.
     * This constructor will always attempt to connect with a medium timeout, create an OutputStream for this socket and set a Medium timeout on
     * future operations, but will NOT immediately create an InputStream.
     * This is to allow a caller to potentially provide a custom URL ClassLoader through
     * {@link #initialiseInputStreamWithComponentLoader(URLClassLoader)}.
     *
     * @param hostname                          The network hostname or IP of the peer we would like to connect to.
     * @param port                              The port number we would like to initialise a connection on.
     * @param requiredSocketStreamHeaderContent Variadic args for what to write down the socket on connection, and expect to read from the socket
     *                                          on connection.
     *                                          Passing nothing and treating this constructor as if this variadic did not exist, will leave the
     *                                          DEFAULT (In/Out)putStream header behaviour in place.
     *                                          Passing a type-casted {@code (Object[]) null}, will disable any read/write operations within the
     *                                          streams initialisations.
     *                                          Passing any other variadic data, will write those objects in order and expect to read the same from
     *                                          the other side on initialisation.
     * @throws IOException This can be thrown if there was an issue creating the output streams, or defaulting the socket operation timeouts.
     */
    public PortableSocket(@NonNull final String hostname, final int port, final Serializable... requiredSocketStreamHeaderContent) throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(hostname, port), THREE_SECONDS_IN_MS);
        this.requiredSocketStreamHeaderContent = requiredSocketStreamHeaderContent;
        setMediumTimeout();
        this.outputStream = getOutputStream();
    }

    /**
     * This method should act as an enhanced and more reliable version of Java's built in {@link InetAddress#isReachable(int)}.
     * This is because - as is widely documented - in the
     * <a href="https://download.java.net/java/early_access/jdk21/docs/api/java.base/java/net/InetAddress.html#isReachable(int)">Java documentation</a>
     * and countless stackoverflow posts, the baked in {@link InetAddress#isReachable(int)} will only make best-effort to perform a regular ICMP PING
     * request. However, certain operating systems may only allow these calls if the JVM is running with ROOT/ADMIN privileges, which on most
     * Windows and MacOS systems it won't be by default.
     * <p></p>
     * This implementation goes the step further by first calling this {@link InetAddress#isReachable(int)}, but if it fails, actually attempting
     * to create the connection - which follows a different workflow in the JVM network stack regarding the permissions it asks for, (I.e. NOT
     * asking the system to perform ICMP control operations).
     * This method of checking can validate both address resolution, as well as actual route traversal to the remote host.
     *
     * @param hostname The hostname of the host we would like to check connectivity to.
     * @param port     The port on that hostname which we would like to check connectivity to.
     * @return True if the host is reachable, False otherwise.
     * @throws IOException Thrown if there was an error setting up the Sockets, a timeout occurred, or the host was otherwise unreachable.
     */
    public static boolean portableIsReachable(@NonNull final String hostname, final int port) throws IOException {

        if (isLocalHostname(hostname)) {
            return true;
        }

        try (Socket connectivityTestSocket = new Socket()) {

            InetAddress inetAddress = InetAddress.getByName(hostname);
            if (inetAddress.isReachable(THREE_SECONDS_IN_MS)) {
                return true;
            }

            InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, port);
            connectivityTestSocket.connect(inetSocketAddress, THREE_SECONDS_IN_MS);

            // We don't need to actually do anything on the socket, since we have now confirmed the connection was successful, so just return True,
            // which will trigger the try-with-resources to close the connection.
            return true;
        } catch (IOException e) {
            throw new IOException("Failed to initiate a connection to: {" + hostname + ":" + port + "} host is unreachable.", e);
        }
    }

    /**
     * Used to determine whether a given hostname is local to the system which is currently running this code.
     *
     * @param hostname The String to check whether it is a locally addressable hostname
     * @return True if the String provided is a local hostname, False otherwise.
     * @throws IOException Thrown if there was some networking stack issue in determining whether this hostname is local, or where the string
     *                     provided could never be a valid hostname.
     */
    public static boolean isLocalHostname(@NonNull final String hostname) throws IOException {

        // Quick and dirty check without having to do any network look-ups first - if this method is being called a lot, it can speed things up a lot.
        if (LOCALHOST_NAMES.contains(hostname)) {
            return true;
        }

        InetAddress inetAddress = InetAddress.getByName(hostname);
        return (inetAddress.isAnyLocalAddress() || inetAddress.isLoopbackAddress() || InetAddress.getLocalHost().equals(inetAddress));
    }

    /**
     * Used to initialise the InputStream for this PortableSocket using a custom provided URLClassLoader, such that it can interpret the kinds of new
     * Objects which this PortableSocket will be used to receive.
     * <p></p>
     * This method should only ever be called once per PortableSocket, and must be called before any calls to {@link #getInputStream()}, as that
     * will attempt default initialisation (without your custom URL Classloader) if the Stream is not already initialised.
     * If this method is being called for the first time initialisation, then any potential Stream Header information provided to the Constructor
     * of this PortableSocket, will attempt to be READ on initialisation before successfully returning the Stream back to the caller.
     *
     * @param componentLoaderForInputStream The URLClassLoader which the InputStream for this PortableSocket should use when parsing data back into
     *                                      Objects.
     * @return The successfully initialised InputStream ready to be read from.
     * @throws IOException Thrown if the Stream has already been initialised elsewhere, or if any potential Stream Header information was not read
     *                     as expected.
     */
    public ObjectInputStream initialiseInputStreamWithComponentLoader(@NonNull final URLClassLoader componentLoaderForInputStream) throws IOException {
        if (inputStream != null) {
            throw new SocketException("Input Stream already initialised, cannot be re-initialised.");
        }

        try {
            inputStream = new ObjectInputStreamWithClassLoader(
                    this.socket.getInputStream(),
                    componentLoaderForInputStream
            ) {
                @Override
                protected void readStreamHeader() throws IOException {
                    if (requiredSocketStreamHeaderContent == null) {
                        return;
                    }
                    if (requiredSocketStreamHeaderContent.length == 0) {
                        super.readStreamHeader();
                        return;
                    }
                    Object readObject;
                    for (Object o : requiredSocketStreamHeaderContent) {
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
            };
        } catch (final IOException e) {
            throw new IOException("Failed to properly initialise a PortableSocket's input stream", e);
        }

        return getInputStream();
    }

    /**
     * Used to acquire the reference to this Portable Socket's OUTPUT stream. Only one output stream is created per PortableSocket.
     *
     * @return The OutputStream associated with this PortableSocket.
     * @throws IOException Thrown if this was the first time this method was called since constructing the Portable Socket, and there was an error
     *                     setting up the OutputStream - such as the Socket being immediately closed, or being unable to write an uninterrupted Stream Header to the
     *                     remote end of the Socket.
     */
    public ObjectOutputStream getOutputStream() throws IOException {
        if (outputStream == null) {
            outputStream = new ObjectOutputStream(
                    socket.getOutputStream()
            ) {
                @Override
                protected void writeStreamHeader() throws IOException {
                    if (requiredSocketStreamHeaderContent == null) {
                        return;
                    }
                    if (requiredSocketStreamHeaderContent.length == 0) {
                        super.writeStreamHeader();
                        return;
                    }
                    for (Object o : requiredSocketStreamHeaderContent) {
                        super.writeObject(o);
                    }
                }
            };
        }

        return outputStream;
    }

    /**
     * Used to acquire the reference to this Portable Socket's INPUT stream. Only one input stream is created per PortableSocket.
     *
     * @return The InputStream associated with this PortableSocket.
     * @throws IOException Thrown if this was the first time this method was called since constructing the Portable Socket, and there was an error
     *                     setting up the InputStream - such as the Socket being immediately closed, or reading a Corrupt Stream Header from the remote end of the Socket.
     */
    public ObjectInputStream getInputStream() throws IOException {
        if (inputStream == null) {
            return initialiseInputStreamWithComponentLoader(new URLClassLoader(new URL[]{}));
        }

        return inputStream;
    }

    @Override
    public void close() {
        try {
            try {
                socket.getInputStream().close();
            } catch (IOException os) {
                // Ignore as going to terminate anyway
            }
            try {
                socket.getOutputStream().close();
            } catch (IOException os) {
                // Ignore as going to terminate anyway
            }
            if (!socket.isClosed()) {
                outputStream.flush();
                socket.close();
            }
        } catch (IOException e) {
            // ignore as going to terminate anyway.
        }
    }

    /**
     * Calls through to {@link Socket#setSoTimeout(int)} with a default 'short' value.
     *
     * @return The previous value which would have been returned by {@link Socket#getSoTimeout()} before calling this method.
     * @throws SocketException Can be thrown for example if the Socket you are trying to set timeouts on, is closed.
     */
    public int setFastTimeout() throws SocketException {
        return setOneSecondTimeout();
    }

    /**
     * Calls through to {@link Socket#setSoTimeout(int)} with a default 'medium' value.
     *
     * @return The previous value which would have been returned by {@link Socket#getSoTimeout()} before calling this method.
     * @throws SocketException Can be thrown for example if the Socket you are trying to set timeouts on, is closed.
     */
    public int setMediumTimeout() throws SocketException {
        return setThreeSecondTimeout();
    }

    /**
     * Calls through to {@link Socket#setSoTimeout(int)} with a default 'long' value.
     *
     * @return The previous value which would have been returned by {@link Socket#getSoTimeout()} before calling this method.
     * @throws SocketException Can be thrown for example if the Socket you are trying to set timeouts on, is closed.
     */
    public int setLongTimeout() throws SocketException {
        return setTenSecondTimeout();
    }

    /**
     * Calls through to {@link Socket#setSoTimeout(int)} with a default 'short' value.
     *
     * @return The previous value which would have been returned by {@link Socket#getSoTimeout()} before calling this method.
     * @throws SocketException Can be thrown for example if the Socket you are trying to set timeouts on, is closed.
     */
    public int setOneSecondTimeout() throws SocketException {
        int previousTimeout = socket.getSoTimeout();
        socket.setSoTimeout(ONE_SECOND_IN_MS);
        return previousTimeout;
    }

    /**
     * Calls through to {@link Socket#setSoTimeout(int)} with a default 'short' value.
     *
     * @return The previous value which would have been returned by {@link Socket#getSoTimeout()} before calling this method.
     * @throws SocketException Can be thrown for example if the Socket you are trying to set timeouts on, is closed.
     */
    public int setTwoSecondTimeout() throws SocketException {
        int previousTimeout = socket.getSoTimeout();
        socket.setSoTimeout(TWO_SECONDS_IN_MS);
        return previousTimeout;
    }

    /**
     * Calls through to {@link Socket#setSoTimeout(int)} with a default 'medium' value.
     *
     * @return The previous value which would have been returned by {@link Socket#getSoTimeout()} before calling this method.
     * @throws SocketException Can be thrown for example if the Socket you are trying to set timeouts on, is closed.
     */
    public int setThreeSecondTimeout() throws SocketException {
        int previousTimeout = socket.getSoTimeout();
        socket.setSoTimeout(THREE_SECONDS_IN_MS);
        return previousTimeout;
    }

    /**
     * Calls through to {@link Socket#setSoTimeout(int)} with a default 'medium' value.
     *
     * @return The previous value which would have been returned by {@link Socket#getSoTimeout()} before calling this method.
     * @throws SocketException Can be thrown for example if the Socket you are trying to set timeouts on, is closed.
     */
    public int setFiveSecondTimeout() throws SocketException {
        int previousTimeout = socket.getSoTimeout();
        socket.setSoTimeout(FIVE_SECONDS_IN_MS);
        return previousTimeout;
    }

    /**
     * Calls through to {@link Socket#setSoTimeout(int)} with a default 'long' value.
     *
     * @return The previous value which would have been returned by {@link Socket#getSoTimeout()} before calling this method.
     * @throws SocketException Can be thrown for example if the Socket you are trying to set timeouts on, is closed.
     */
    public int setTenSecondTimeout() throws SocketException {
        int previousTimeout = socket.getSoTimeout();
        socket.setSoTimeout(TEN_SECONDS_IN_MS);
        return previousTimeout;
    }

    /**
     * Calls through to {@link Socket#setSoTimeout(int)} with a default 'long' value.
     *
     * @return The previous value which would have been returned by {@link Socket#getSoTimeout()} before calling this method.
     * @throws SocketException Can be thrown for example if the Socket you are trying to set timeouts on, is closed.
     */
    public int setThirtySecondTimeout() throws SocketException {
        int previousTimeout = socket.getSoTimeout();
        socket.setSoTimeout(3 * TEN_SECONDS_IN_MS);
        return previousTimeout;
    }

    /**
     * Calls through to {@link Socket#setSoTimeout(int)} with a value of 0, which is interpreted as limitless.
     *
     * @return The previous value which would have been returned by {@link Socket#getSoTimeout()} before calling this method.
     * @throws SocketException Can be thrown for example if the Socket you are trying to set timeouts on, is closed.
     */
    public int setNoTimeout() throws SocketException {
        int previousTimeout = socket.getSoTimeout();
        socket.setSoTimeout(NO_TIMEOUT);
        return previousTimeout;
    }

    /**
     * Calls through to {@link Socket#setSoTimeout(int)} with a custom provided value.
     *
     * @param timeoutInMilliseconds The amount of milliseconds which should be allowed to pass before read operations on this Socket's streams
     *                              should throw timeout exceptions.
     * @return The previous value which would have been returned by {@link Socket#getSoTimeout()} before calling this method.
     * @throws SocketException Can be thrown for example if the Socket you are trying to set timeouts on, is closed.
     */
    public int setCustomTimeoutInMs(final int timeoutInMilliseconds) throws SocketException {
        int previousTimeout = socket.getSoTimeout();
        socket.setSoTimeout(timeoutInMilliseconds);
        return previousTimeout;
    }
}
