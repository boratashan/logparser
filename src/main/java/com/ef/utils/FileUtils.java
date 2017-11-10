package com.ef.utils;

import java.io.File;
/**
 * Helper methods for file and i/o operations.
 * @author  Bora Tashan
 * @version 1.0
 * @since   2017-1-23
 */
public class FileUtils {
    public static boolean isFileExisted(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    public static String addPathToFileNameIfNeeded(String path, String fileName) {
        if(!(isFileNameWithoutPath(fileName)))
            return fileName;
        else {
            if(path.endsWith("/")||path.endsWith("\\"))
                return path.concat(fileName);
            else
                return path.concat("/").concat(fileName);
        }
    }

    public static boolean isFileNameWithoutPath(String fileName) {
        return (!(fileName.contains("\\") || fileName.contains("/")));
    }
}
