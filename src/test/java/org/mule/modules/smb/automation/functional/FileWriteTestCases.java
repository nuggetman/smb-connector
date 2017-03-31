package org.mule.modules.smb.automation.functional;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class FileWriteTestCases extends AbstractTestCase<SmbConnector> {

    public FileWriteTestCases() {
        super(SmbConnector.class);
    }

    @Before
    public void setup() {
        // TODO
    }

    @After
    public void tearDown() {
        // TODO
    }

    @Test
    public void verify() {
        boolean expected = false;
        java.lang.String fileName = null;
        boolean append = false;
        Object fileContent = null;
        assertEquals(getConnector().fileWrite(fileName, append, fileContent), expected);
    }

}