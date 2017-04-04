package org.mule.modules.smb.automation.functional;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class FileReadTestCases extends AbstractTestCase<SmbConnector> {

    public FileReadTestCases() {
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
        byte[] expected = null;
        java.lang.String fileName = null;
        java.lang.Integer fileAge = null;
        boolean autoDelete = false;
        try {
            assertEquals(getConnector().fileRead(fileName, fileAge, autoDelete), expected);
        } catch (ConnectionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}