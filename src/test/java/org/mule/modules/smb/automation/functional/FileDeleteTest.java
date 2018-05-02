package org.mule.modules.smb.automation.functional;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class FileDeleteTest extends AbstractTestCase<SmbConnector> {

    private String fileName = new String("testdeletefile.txt");
    private String fileContent = new String("somecontent");

    public FileDeleteTest() {
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

    @Test
    public void verify() {
        try {
            getConnector().fileDelete(fileName);
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}