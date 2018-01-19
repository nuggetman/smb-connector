package org.mule.modules.smb.automation.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mule.modules.smb.utils.Utilities;

public class UtilitiesUnitTest {

    @Test
    public void normalizePathTest1() {
        assertEquals("/path1/", Utilities.normalizePath("//path1/"));
    }

    @Test
    public void normalizePathTest2() {
        assertEquals("/path1/", Utilities.normalizePath("/path1"));
    }

    @Test
    public void normalizePathTest3() {
        assertEquals("/path1/", Utilities.normalizePath("path1/"));
    }

    @Test
    public void normalizeFileTest1() {
        assertEquals("/file1.txt", Utilities.normalizeFile("//file1.txt"));
    }

    @Test
    public void normalizeFileTest2() {
        assertEquals("/file1.txt", Utilities.normalizeFile("/file1.txt/"));
    }

    @Test
    public void normalizeFileTest3() {
        assertEquals("/file1.txt", Utilities.normalizeFile("file1.txt"));
    }

    @Test
    public void isNotBlankOrEmptyOrNullTest() {
        assertTrue(Utilities.isNotBlankOrEmptyOrNull("file1.txt"));
        assertFalse(Utilities.isNotBlankOrEmptyOrNull(""));
        assertFalse(Utilities.isNotBlankOrEmptyOrNull(null));
    }

}
