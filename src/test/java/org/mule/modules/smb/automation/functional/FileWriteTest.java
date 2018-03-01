package org.mule.modules.smb.automation.functional;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class FileWriteTest extends AbstractTestCase<SmbConnector> {

    private String fileName = new String("testwritefile.txt");
    private String fileContent = new String("somecontent");
    private Integer integerContent = 999999999;

    public FileWriteTest() {
        super(SmbConnector.class);
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
    public void verifyWriteNoAppend() {
        try {
            getConnector().fileWrite(fileName, false, fileContent, "UTF-8");
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verifyWriteAppendString() {
        try {
            getConnector().fileWrite(fileName, true, fileContent, "UTF-8");
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verifyWriteAppendInputStream() {
        try {
            getConnector().fileWrite(fileName, true, IOUtils.toInputStream(fileContent), "UTF-8");
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verifyWriteAppendByteArray() {
        try {
            getConnector().fileWrite(fileName, true, fileContent.getBytes(), "UTF-8");
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verifyWriteAppendInteger() {
        try {
            getConnector().fileWrite(fileName, true, integerContent, "UTF-8");
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}