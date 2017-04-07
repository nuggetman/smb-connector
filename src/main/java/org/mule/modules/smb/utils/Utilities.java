package org.mule.modules.smb.utils;

import org.mule.util.StringUtils;

public class Utilities {

    /**
     * 
     * Helper class to normalize naming for SMB connectivity
     * 
     * @param path
     *            String path name to normalize
     * @return String of normalized path
     */

    public static String normalizePath(String path) {

        StringBuilder sb = new StringBuilder(path.replaceAll("(//)", "/"));
        if (!path.startsWith("/")) {
            sb.insert(0, "/");
        }
        if (!path.endsWith("/")) {
            sb.append("/");
        }
        return sb.toString();
    }

    /**
     * 
     * Helper class to normalize naming for SMB connectivity
     * 
     * @param file
     *            file name to normalize
     * @return String of normalized file
     */
    public static String normalizeFile(String file) {

        StringBuilder sb = new StringBuilder(file.replaceAll("//", "/"));
        if (!file.startsWith("/")) {
            sb.insert(0, "/");
        }
        if (file.endsWith("/")) {
            sb.replace(sb.length() - 1, sb.length(), "");
        }
        return sb.toString();
    }

    /**
     * 
     * Helper class to validate whether a string is usable
     * 
     * @param string
     *            String to check
     * @return true if string is valid
     */
    public static boolean isNotBlankOrEmptyOrNull(String string) {
        if (string != null) {
            return StringUtils.isNotEmpty(string);
        } else {
            return false;
        }
    }

    /**
     * Do not allow creation of this class
     */
    private Utilities() {
        throw new IllegalAccessError("Utility class");
    }

}
