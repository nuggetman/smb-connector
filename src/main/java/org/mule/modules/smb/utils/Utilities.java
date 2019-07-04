package org.mule.modules.smb.utils;

import org.mule.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hierynomus.msdtyp.FileTime;

public class Utilities {
	
	private static final Logger logger = LoggerFactory.getLogger(Utilities.class);

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
     * 
     * @param target
     *            int value to target in ms
     * @param age
     *            epoch long value of file in ms
     * 
     * @param minimumAge
     *            long value in ms of minimum file age
     * @return
     */
    public static boolean timeCompare(int target, long age) {
        boolean ready = false;
        if (target == 0) {
            logger.debug("Time check skipped");
            ready = true;
        } else if (target > 0) {
            long currentAge = FileTime.now().toEpochMillis() - age;
            if (currentAge < 0) {
                logger.warn("The system clocks appear to be out of sync, either time or timezone");
            } else if (currentAge > 0 && currentAge < target) {
                logger.debug("Target is not ready yet");
            } else if (currentAge > 0 && currentAge > target) {
                ready = true;
            }
        }
        return ready;
    }

    /**
     * Do not allow creation of this class
     */
    private Utilities() {
        throw new IllegalAccessError("Utility class");
    }

}
