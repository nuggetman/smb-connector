package org.mule.modules.smb.automation.functional;

import org.junit.Before;
import org.junit.Test;
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
            getConnector().fileWrite(fileName, null, false, fileContent.getBytes(), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verify() {
        try {
            getConnector().fileDelete(fileName, null);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}