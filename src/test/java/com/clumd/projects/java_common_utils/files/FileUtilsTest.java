package com.clumd.projects.java_common_utils.files;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class FileUtilsTest {

    @Test
    void get_file_as_string() throws IOException {
        assertEquals("{" + System.lineSeparator() + "  \"is testing\": true" + System.lineSeparator() + "}" + System.lineSeparator(), FileUtils.getFileAsString("src/test/resources/files/testConfigFile.json"));
    }

    @Test
    void get_file_as_strings() throws IOException {
        ArrayList<String> testConfig = new ArrayList<>(3);
        testConfig.add("{" + System.lineSeparator());
        testConfig.add("  \"is testing\": true" + System.lineSeparator());
        testConfig.add("}" + System.lineSeparator());
        assertEquals(testConfig, FileUtils.getFileAsStrings("src/test/resources/files/testConfigFile.json"));
    }

    @Test
    void get_resource_as_string() throws IOException {
        final String asString = FileUtils.getLocalResourceAsString("com/clumd/projects/java_common_utils/files/FileUtils.class");

        assertEquals(7444, asString.length(), 0);
        assertTrue(asString.contains("com/clumd/projects/java_common_utils/files/FileUtils"));
    }

    @Test
    void get_resource_as_strings() throws IOException {
        final List<String> asStrings = FileUtils.getLocalResourceAsStrings("com/clumd/projects/java_common_utils/files/FileUtils.class");

        assertEquals(72, asStrings.size(), 0);
        assertTrue(asStrings.get(4).contains("com/clumd/projects/java_common_utils/files/FileUtils"));
    }

    @Test
    void get_resource_as_strings_not_found() {
        try {
            FileUtils.getLocalResourceAsStrings("com/clumd/projects/java_common_utils/files/F");
            fail("The previous line should have thrown an exception.");
        } catch (IOException e) {
            assertEquals("Unable to read resource from stream, check relative class path hierarchy.", e.getMessage());
        }
    }

    @Test
    void test_file_not_found() throws IOException {
        try {
            FileUtils.getFileAsString("src/test/resources/files/logs/thisFileIsNotFound.json");
            fail("The previous line should have thrown an exception.");
        } catch (FileNotFoundException e) {
            assertEquals("src/test/resources/files/logs/thisFileIsNotFound.json (File not Found)", e.getMessage());
        }
    }

    @Test
    void test_file_is_directory_1() throws IOException {
        try {
            FileUtils.getFileAsString("src/test/resources/files/logs/");
            fail("The previous line should have thrown an exception.");
        } catch (FileNotFoundException e) {
            assertEquals("src/test/resources/files/logs/ (Is a directory, not a file)", e.getMessage());
        }
    }

    @Test
    void test_file_is_directory_2() throws IOException {
        try {
            FileUtils.getFileAsString("src/test/resources/files/logs");
            fail("The previous line should have thrown an exception.");
        } catch (FileNotFoundException e) {
            assertEquals("src/test/resources/files/logs (Is a directory, not a file)", e.getMessage());
        }
    }

    @Test
    void test_loading_file_as_bytes() throws IOException {
        byte[] bytes = FileUtils.getFileAsBytes("src/test/resources/files/testConfigFile.json");
        assertTrue(new String(bytes).startsWith("{" + System.lineSeparator() + "  \"is testing\": true" + System.lineSeparator() + "}" + System.lineSeparator()));
    }

    @Test
    void test_loading_null_file() throws IOException {
        byte[] bytes = FileUtils.getFileAsBytes(null);
        assertEquals(0, bytes.length, 0);
    }

    @Test
    void test_non_fs_location() {
        try {
            FileUtils.validateIsDirectory("\0");
            fail("The previous line should have thrown an exception.");
        } catch (IOException e) {
            assertEquals("The path provided could not be resolved to a file system location.", e.getMessage());
        }
    }

    @Test
    void test_directory_does_not_exist() {
        try {
            FileUtils.validateIsDirectory("src/test/resources/files/notExist");
            fail("The previous line should have thrown an exception.");
        } catch (IOException e) {
            assertEquals("The path provided does not exist.", e.getMessage());
        }
    }

    @Test
    void test_directory_is_actually_file() {
        try {
            FileUtils.validateIsDirectory("src/test/resources/files/logs/placeholder.txt");
            fail("The previous line should have thrown an exception.");
        } catch (IOException e) {
            assertEquals("The path provided is not a directory.", e.getMessage());
        }
    }

//    @Test
//    void test_read_directory_denied() {
//        String path = "src/test/resources/files/deniedRead";
//        new File(path).setReadable(true, false);
//        new File(path).setWritable(true, false);
//        new File(path).setExecutable(true, false);
//        try {
//            FileUtils.validateIsDirectory(path);
//            fail("The previous line should have thrown an exception.");
//        } catch (IOException e) {
//            assertEquals("The directory provided cannot be read from.", e.getMessage());
//        }
//    }

    @Test
    void test_directory_is_valid() {
        String path = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "files" + File.separator + "logs";
        try {
            String validDirectoryPath = FileUtils.validateIsDirectory(path);
            assertNotNull(validDirectoryPath);
            assertTrue(validDirectoryPath.strip().length() > 0);
            assertTrue(validDirectoryPath.length() > path.length());
            assertTrue(validDirectoryPath.contains(path));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void test_writing_file_creates_if_not_exists() throws IOException {
        String path = "src/test/resources/files/" + "not_exists.txt";
        String data = "hello" + System.lineSeparator();

        try {
            FileUtils.getFileAsString(path);
            fail("The previous method should have thrown an exception.");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("(File not Found)"));
        }

        FileUtils.writeStringToFile(data, path, false);
        assertEquals("hello" + System.lineSeparator(), FileUtils.getFileAsString(path));
        assertTrue(new File(path).delete());

        try {
            FileUtils.getFileAsString(path);
            fail("The previous method should have thrown an exception.");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("(File not Found)"));
        }
    }

    @Test
    void test_writing_string_to_file_overwrite() throws IOException {
        String path = "src/test/resources/files/" + "test_file_" + UUID.randomUUID() + ".txt";
        String existingData = "I should disappear";
        String data = "hello" + System.lineSeparator() + "world" + System.lineSeparator();

        FileUtils.writeStringToFile(existingData, path, false);
        FileUtils.writeStringToFile(data, path, false);
        assertEquals(data, FileUtils.getFileAsString(path));

        assertTrue(new File(path).delete());
    }

    @Test
    void test_writing_string_to_file_append() throws IOException {
        String path = "src/test/resources/files/" + "test_file_" + UUID.randomUUID() + ".txt";
        String data = "hello" + System.lineSeparator() + "world" + System.lineSeparator();

        FileUtils.writeStringToFile(data, path, true);
        FileUtils.writeStringToFile(data, path, true);
        assertEquals(data + data, FileUtils.getFileAsString(path));

        assertTrue(new File(path).delete());
    }

    @Test
    void test_writing_strings_to_file_overwrite() throws IOException {
        String path = "src/test/resources/files/" + "test_file_" + UUID.randomUUID() + ".txt";
        Collection<String> existingData = List.of("I should" + System.lineSeparator(), "disappear" + System.lineSeparator());
        Collection<String> data = List.of("hello" + System.lineSeparator(), "world" + System.lineSeparator());

        FileUtils.writeStringsToFile(existingData, path, false);
        FileUtils.writeStringsToFile(data, path, false);
        assertEquals(data, FileUtils.getFileAsStrings(path));

        assertTrue(new File(path).delete());
    }

    @Test
    void test_writing_strings_to_nested_file_overwrite() throws IOException {
        String path = "src/test/resources/files/more/things/are/here/now/" + "test_file_" + UUID.randomUUID() + ".txt";
        Collection<String> existingData = List.of("I should" + System.lineSeparator(), "disappear" + System.lineSeparator());
        Collection<String> data = List.of("hello" + System.lineSeparator(), "world" + System.lineSeparator());

        FileUtils.writeStringsToFile(existingData, path, false);
        FileUtils.writeStringsToFile(data, path, false);
        assertEquals(data, FileUtils.getFileAsStrings(path));

        FileUtils.deleteDirectoryIfExists("src/test/resources/files/more");
    }

    @Test
    void test_writing_strings_to_file_append() throws IOException {
        String path = "src/test/resources/files/" + "test_file_" + UUID.randomUUID() + ".txt";
        Collection<String> data = List.of("hello" + System.lineSeparator(), "world" + System.lineSeparator());

        FileUtils.writeStringsToFile(data, path, true);
        FileUtils.writeStringsToFile(data, path, true);
        Collection<String> actualStrings = FileUtils.getFileAsStrings(path);
        assertEquals(data.size() * 2, actualStrings.size(), 0);
        assertTrue(actualStrings.containsAll(data));
        for (String s : actualStrings) {
            assertTrue(data.contains(s));
        }

        assertTrue(new File(path).delete());
    }

    @Test
    void test_writing_bytes_to_file_overwrite() throws IOException {
        String path = "src/test/resources/files/" + "test_file_" + UUID.randomUUID() + ".txt";
        byte[] existingData = new byte[]{11, 12, 13, 14, 15};
        byte[] data = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        FileUtils.writeBytesToFile(existingData, path, false);
        FileUtils.writeBytesToFile(data, path, false);

        byte[] actualData = FileUtils.getFileAsBytes(path);
        assertEquals(data.length, actualData.length, 0);

        for (int i = 0; i < actualData.length; i++) {
            assertEquals(data[i], actualData[i]);
        }

        assertTrue(new File(path).delete());
    }

    @Test
    void test_writing_bytes_to_file_append() throws IOException {
        String path = "src/test/resources/files/" + "test_file_" + UUID.randomUUID() + ".txt";
        byte[] data = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        byte[] doubleData = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        FileUtils.writeBytesToFile(data, path, true);
        FileUtils.writeBytesToFile(data, path, true);

        byte[] actualData = FileUtils.getFileAsBytes(path);

        assertEquals(doubleData.length, actualData.length, 0);

        for (int i = 0; i < actualData.length; i++) {
            assertEquals(doubleData[i], actualData[i]);
        }

        assertTrue(new File(path).delete());
    }

    @Test
    void test_delete_file_doesnt_allow_delete_dir() throws IOException {
        try {
            FileUtils.deleteFileIfExists("src/test/resources/files");
            fail("The previous method call should have thrown an exception.");
        } catch (FileNotFoundException e) {
            assertTrue(e.getMessage().contains(" (Is a directory, not a file)"));
        }
    }

    @Test
    void test_delete_dir_doesnt_allow_delete_file() {
        try {
            FileUtils.deleteDirectoryIfExists("src/test/resources/files/testConfigFile.json");
            fail("The previous method call should have thrown an exception.");
        } catch (IOException e) {
            assertEquals("The path provided is not a directory.", e.getMessage());
        }
    }
}
