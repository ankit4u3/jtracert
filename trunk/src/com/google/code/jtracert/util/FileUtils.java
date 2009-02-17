package com.google.code.jtracert.util;

import java.io.File;
import java.io.IOException;

/**
 * @author Dmitry Bedrin
 */
public class FileUtils {

    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static String FILE_SEPARATOR = System.getProperty("file.separator");
    public final static String TEMP_DIR = System.getProperty("java.io.tmpdir");

    public static void forceMkdir(File directory) throws IOException {
        if (directory.exists()) {
            if (directory.isFile()) {
                throw new IOException("File " + directory + " exists and is not a directory. Unable to create directory.");
            }
        } else {
            if (!directory.mkdirs()) {
                throw new IOException("Unable to create directory " + directory);
            }
        }
    }

}
