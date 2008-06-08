package com.google.code.jtracert.util;

import java.io.IOException;
import java.io.File;

/**
 * @author dmitry.bedrin
 */
public class FileUtils {

    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static String FILE_SEPARATOR = System.getProperty("file.separator");

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
