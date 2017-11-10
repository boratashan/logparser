package com.ef.utils;

import java.io.File;
import java.net.URISyntaxException;
/**
 * General application helper functions.
 * @author  Bora Tashan
 * @version 1.0
 * @since   2017-1-23
 */
public class ApplicationUtils {
    public static String getJarRunningPath() throws URISyntaxException {
        File f = new File(ApplicationUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        if (f.getPath().endsWith(".jar"))
            return f.getParent();
        else
            return f.getPath();
    }

    public static String getFileNameUnderPath(String path, String nameOfFile) {
        if (path.endsWith("\\") || path.endsWith("/"))
            return path.concat(nameOfFile);
        else
            return path.concat("/").concat(nameOfFile);

    }
}
