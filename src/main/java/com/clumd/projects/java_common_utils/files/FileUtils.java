package com.clumd.projects.java_common_utils.files;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * A collection of useful methods to call for various File operations I find myself needing to do frequently.
 */
public class FileUtils {

    private FileUtils() {
    }

    /**
     * This method takes a file path, and concatenates all the content into a single string.
     *
     * @param file The file path for the file to turn into a string
     * @return The file as a single string.
     * @throws IOException Thrown if it could not find, or you don't have permissions for that file.
     */
    public static String getFileAsString(final String file) throws IOException {
        StringBuilder ret = new StringBuilder();

        for (String s : getFileAsStrings(file)) {
            ret.append(s);
        }

        return ret.toString();
    }

    /**
     * This method takes a file path, and returns an array of strings for each line in the file.
     *
     * @param file The file path for the file to turn into string array
     * @return An element for each line in the file.
     * @throws IOException Thrown if it could not find, or you don't have permissions for that file.
     */
    public static List<String> getFileAsStrings(final String file) throws IOException {
        checkIfExistsOrIsFolder(file);
        ArrayList<String> ret = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                ret.add(reader.readLine() + System.lineSeparator());
            }
        }

        return ret;
    }

    /**
     * This method checks a given input path to ensure it exists and is a file.
     *
     * @param path The path to check.
     * @throws FileNotFoundException Thrown if the path has no file at it, or the path ends at an existing directory.
     */
    private static void checkIfExistsOrIsFolder(final String path) throws FileNotFoundException {
        File file = new File(path);

        if (!file.exists()) {
            throw new FileNotFoundException(path + " (File not Found)");
        }

        if (file.isDirectory()) {
            throw new FileNotFoundException(path + " (Is a directory, not a file)");
        }
    }

    /**
     * This is used to return the raw bytes of a given file.
     *
     * @param path The full path to the file to get the bytes from.
     * @return The byte[] of the entire file contents.
     * @throws IOException Thrown if there was a problem accessing the requested file.
     */
    public static byte[] getFileAsBytes(final String path) throws IOException {
        if (path == null) {
            return new byte[]{};
        }

        checkIfExistsOrIsFolder(path);

        return Files.readAllBytes(new File(path).toPath());
    }

    /**
     * This is used to get a file part of the source as a string.
     *
     * @param resourceName The name of the resource to get as a string
     * @return The string value of the resource.
     * @throws IOException Thrown if there was a problem accessing the requested file
     */
    public static String getLocalResourceAsString(final String resourceName) throws IOException {
        StringBuilder ret = new StringBuilder();

        for (String s : getLocalResourceAsStrings(resourceName)) {
            ret.append(s);
        }

        return ret.toString();
    }

    /**
     * This is used to get the contents of a file line by line.
     *
     * @param resourceName The name of the resource to get as a String array.
     * @return The line by line values of the resource.
     * @throws IOException Thrown if there was a problem accessing the requested file
     */
    public static List<String> getLocalResourceAsStrings(final String resourceName) throws IOException {
        ArrayList<String> ret = new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(
                             Objects.requireNonNull(
                                     FileUtils.class.getClassLoader()
                                             .getResourceAsStream(resourceName)
                             )
                     ))
        ) {
            while (reader.ready()) {
                ret.add(reader.readLine() + '\n');
            }
        } catch (NullPointerException e) {
            throw new IOException("Unable to read resource from stream, check relative class path hierarchy.");
        }

        return ret;
    }

    /**
     * This is used to validate the existence (as a file) and permission to read.
     *
     * @param path The path to validate.
     * @return The String of the resolved path to that file (removal of . and .. if necessary)
     * @throws IOException Thrown if there was a problem accessing that path as a file.
     */
    public static File validateIsFile(final String path) throws IOException {
        checkIfExistsOrIsFolder(path);

        File f = new File(path).getCanonicalFile();

        //check attributes
        if (!f.canRead()) {
            throw new IOException("The file provided cannot be read from.");
        }

        //return valid path
        return f;
    }

    /**
     * This is used to validate the existence (as a directory) and permission to read from the given path.
     *
     * @param path The path to validate.
     * @return The String of the resolved path to that directory (removal of . and .. if necessary)
     * @throws IOException Thrown if there was a problem accessing that path as a directory.
     */
    public static File validateIsDirectory(final String path) throws IOException {
        File activeDir;

        //resolve canonical
        try {
            activeDir = new File(path).getCanonicalFile();
        } catch (IOException e) {
            throw new IOException("The path provided could not be resolved to a file system location.");
        }

        //check attributes
        if (!activeDir.exists()) {
            throw new IOException("The path provided does not exist.");
        }
        if (!activeDir.isDirectory()) {
            throw new IOException("The path provided is not a directory.");
        }
        if (!activeDir.canRead()) {
            throw new IOException("The directory provided cannot be read from.");
        }

        //return valid path
        return activeDir;
    }

    /**
     * Used to write a single string to a file.
     *
     * @param data   The String to be written to the file.
     * @param path   The path of the file that we would like to write into
     * @param append If we should add to the end of the file, or overwrite from the beginning.
     * @throws IOException Thrown if there was a problem writing to the file.
     */
    public static void writeStringToFile(final String data, final String path, final boolean append) throws IOException {
        makeContainingDirs(path);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, append))) {
            writer.write(data);
        }
    }

    /**
     * Used to write multiple strings to a file.
     *
     * @param data   The collection of Strings to be written to the file.
     * @param path   The path of the file that we would like to write into
     * @param append If we should add to the end of the file, or overwrite from the beginning.
     * @throws IOException Thrown if there was a problem writing to the file.
     */
    public static void writeStringsToFile(final Collection<String> data, final String path, final boolean append) throws IOException {
        makeContainingDirs(path);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, append))) {
            for (String s : data) {
                writer.write(s);
            }
        }
    }

    /**
     * Used to write raw bytes to a file.
     *
     * @param data   The data to be written to the file.
     * @param path   The path of the file that we would like to write into
     * @param append If we should add to the end of the file, or overwrite from the beginning.
     * @throws IOException Thrown if there was a problem writing to the file.
     */
    public static void writeBytesToFile(final byte[] data, final String path, final boolean append) throws IOException {
        makeContainingDirs(path);
        try (FileOutputStream writer = new FileOutputStream(path, append)) {
            writer.write(data);
        }
    }

    /**
     * Create all the containing directories for the given path, if not exists.
     * <p>
     * This is a simple pass-through method.
     *
     * @param path The path to create the parent directories to
     * @throws IOException Thrown if there was a problem creating the parent directories.
     */
    public static void makeContainingDirs(String path) throws IOException {
        makeAllDirs(new File(path).getParentFile().getCanonicalPath());
    }

    /**
     * Create all the containing directories INCLUDING the leaf for the given path, if not exists.
     * <p>
     * This is a simple pass-through method.
     *
     * @param path The path to create the parent directories to
     * @throws IOException Thrown if there was a problem creating the parent directories.
     */
    public static void makeAllDirs(String path) throws IOException {
        Files.createDirectories(new File(path).toPath());
    }

    /**
     * Used to delete a file on the filesystem if it exists, if not - no action is taken.
     *
     * @param path The path of the file to be deleted.
     * @throws IOException Thrown if there was a problem deleting the file.
     */
    public static void deleteFileIfExists(String path) throws IOException {
        File file = new File(path);

        if (!file.exists()) {
            return;
        }

        if (file.isDirectory()) {
            throw new FileNotFoundException(path + " (Is a directory, not a file)");
        }

        deleteIfExists(path);
    }

    /**
     * Used to delete a directory on the filesystem if it exists, if not - no action is taken.
     *
     * @param path The path of the directory to be deleted.
     * @throws IOException Thrown if there was a problem deleting the directory.
     */
    public static void deleteDirectoryIfExists(String path) throws IOException {
        File file = new File(path);

        if (!file.exists()) {
            return;
        }

        file = validateIsDirectory(path);

        deleteIfExists(file.getCanonicalPath());
    }

    /**
     * Used to delete an item on the filesystem if it exists, if not - no action is taken.
     *
     * @param path The path of the item to be deleted.
     * @throws IOException Thrown if there was a problem deleting the file/directory.
     */
    public static void deleteIfExists(String path) throws IOException {
        File file = new File(path);

        if (!file.exists()) {
            return;
        }

        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (!Files.isSymbolicLink(f.toPath())) {
                    deleteIfExists(f.getCanonicalPath());
                }
            }
        }
        try {
            Files.delete(file.toPath());
        } catch (Exception e) {
            throw new IOException("Failed to delete: {" + path + "}", e);
        }
    }
}
