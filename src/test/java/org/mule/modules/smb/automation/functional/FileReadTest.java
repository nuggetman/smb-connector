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
    private Integer fileAge = new Integer(500);

    public FileReadTest() {
        super(SmbConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().fileWrite(fileName, false, fileContent.getBytes());
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @After
    public void tearDown() {
        try {
            getConnector().fileDelete(fileName);
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    @Test
    public void verifyFileReadNoDelete() {
        try {
            assertEquals(fileContent, new String(getConnector().fileRead(fileName, fileAge, false)));
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verifyFileReadWithDelete() {
        try {
            assertEquals(fileContent, new String(getConnector().fileRead(fileName, fileAge, true)));
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}