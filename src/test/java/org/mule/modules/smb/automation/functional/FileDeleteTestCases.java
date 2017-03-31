package org.mule.modules.smb.automation.functional;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class FileDeleteTestCases extends AbstractTestCase<SmbConnector> {

    public FileDeleteTestCases() {
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
        assertEquals(getConnector().fileDelete(fileName), expected);
    }

}