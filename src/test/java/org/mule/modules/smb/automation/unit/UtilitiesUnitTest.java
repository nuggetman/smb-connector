package org.mule.modules.smb.automation.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mule.modules.smb.utils.Utilities;

public class UtilitiesUnitTest {

    @Test
    public void normalizePathTest1() {
        assertEquals("path1", Utilities.cleanPath("//path1/"));
    }

    @Test
    public void normalizePathTest2() {
        assertEquals("path1", Utilities.cleanPath("/path1"));
    }

    @Test
    public void normalizePathTest3() {
        assertEquals("path1", Utilities.cleanPath("path1/"));
    }
    
    @Test
    public void normalizePathTest4() {
        assertEquals("path1", Utilities.cleanPath("path1"));
    }
    
    @Test
    public void normalizePathTest5() {
        assertEquals("path1/path2", Utilities.cleanPath("//path1/path2/"));
    }

    @Test
    public void normalizePathTest6() {
        assertEquals("path1/path2", Utilities.cleanPath("/path1/path2"));
    }

    @Test
    public void normalizePathTest7() {
        assertEquals("path1/path2", Utilities.cleanPath("path1//path2"));
    }
    
    @Test
    public void normalizePathTest8() {
        assertEquals("path1/path2", Utilities.cleanPath("path1/path2"));
    }

    @Test
    public void normalizeFileTest1() {
        assertEquals("file1.txt", Utilities.cleanPath("//file1.txt"));
    }

    @Test
    public void normalizeFileTest2() {
        assertEquals("file1.txt", Utilities.cleanPath("/file1.txt/"));
    }
    
    @Test
    public void normalizeFileTest3() {
        assertEquals("file1.txt", Utilities.cleanPath("/file1.txt"));
    }

    @Test
    public void normalizeFileTest4() {
        assertEquals("file1.txt", Utilities.cleanPath("file1.txt"));
    }
    
    @Test
    public void normalizeFileTestNull() {
        assertEquals("", Utilities.cleanPath(null));
    }
    
    @Test
    public void buildPathTestNull() {
        assertEquals(null, Utilities.buildPath(null,null));
    }
    
    @Test
    public void buildPathTestPath1() {
        assertEquals("path", Utilities.buildPath("path",null));
    }
    
    @Test
    public void buildPathTestPath2() {
        assertEquals("file", Utilities.buildPath(null,"file"));
    }
    
    @Test
    public void buildPathTestPath12() {
        assertEquals("path/file", Utilities.buildPath("path","file"));
    }

    @Test
    public void isNotBlankOrEmptyOrNullTest() {
        assertTrue(Utilities.isNotBlankOrEmptyOrNull("file1.txt"));
        assertFalse(Utilities.isNotBlankOrEmptyOrNull(""));
        assertFalse(Utilities.isNotBlankOrEmptyOrNull(null));
    }

}
