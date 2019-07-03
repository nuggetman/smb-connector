package org.mule.modules.smb.utils;

import org.mule.util.StringUtils;

public class Utilities {

    /**
     * 
     * Helper class to normalize naming for SMB connectivity
     * 
     * @param path
     *            file or dir path to normalize
     * @return String of normalized file
     */
    public static String cleanPath(String path) {
        if (path != null) {
			StringBuilder sb = new StringBuilder(path.replaceAll("(/)+", "/"));
			if (path.startsWith("/")) {
				sb.replace(0, 1, "");
			}
			if (path.endsWith("/")) {
				sb.replace(sb.length()-1, sb.length(), "");
			}
			return sb.toString();
		} else {
			return "";
		}
    }
    
    /**
     * 
     * Helper class to concatenate naming for SMB connectivity
     * 
     * @param path1
     *            file or dir path to normalize
     * @param path2
     *            file or dir path to normalize
     * @return String of normalized file
     */
    public static String buildPath(String path1, String path2) {
        if (path1 != null && path2 != null) {
            path1 = cleanPath(path1);
            path2 = cleanPath(path2);
            StringBuilder sb = new StringBuilder(path1 + "/" + path2);
            return sb.toString();
		} else if (path1 == null && path2 != null) {
			return path2;
		} else if (path1 != null && path2 == null) {
			return path1;
		} else {
			return null;
		}
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
