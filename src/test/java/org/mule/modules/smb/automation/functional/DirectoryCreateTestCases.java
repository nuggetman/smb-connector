package org.mule.modules.smb.automation.functional;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class DirectoryCreateTestCases extends AbstractTestCase<SmbConnector> {

    public DirectoryCreateTestCases() {
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
        java.lang.String dirName = null;
        try {
            assertEquals(getConnector().directoryCreate(dirName), expected);
        } catch (ConnectionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}