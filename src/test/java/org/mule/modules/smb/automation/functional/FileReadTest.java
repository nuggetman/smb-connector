package org.mule.modules.smb.automation.functional;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class FileReadTest extends AbstractTestCase<SmbConnector> {

    private String fileName = new String("testreadfile.txt");
    private String fileContent = new String("somecontent");

    public FileReadTest() {
        super(SmbConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().fileWrite(fileName, null, false, fileContent.getBytes(), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @After
    public void tearDown() {
        try {
            getConnector().fileDelete(fileName, null);
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    @Test
    public void verifyFileReadNoDelete() {
        try {
            assertEquals(fileContent, new String(getConnector().fileRead(fileName, null, false)));
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    @Test
    public void verifyFileReadWithDelete() {
        try {
            assertEquals(fileContent, new String(getConnector().fileRead(fileName, null, true)));
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}