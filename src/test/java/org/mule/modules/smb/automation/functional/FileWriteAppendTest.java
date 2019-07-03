package org.mule.modules.smb.automation.functional;

import static org.mule.modules.smb.automation.functional.TestDataBuilder.FILENAME;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.FILE_CONTENT;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class FileWriteAppendTest extends AbstractTestCase<SmbConnector> {

    public FileWriteAppendTest() {
        super(SmbConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().fileWrite(FILENAME, null, false, FILE_CONTENT.getBytes(), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    @After
    public void tearDown() {
        try {
            getConnector().fileDelete(FILENAME, null);
        } catch ( Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verifyWriteAppendString() {
        try {
            getConnector().fileWrite(FILENAME, null, true, FILE_CONTENT, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verifyWriteAppendInputStream() {
        try {
            getConnector().fileWrite(FILENAME, null, true, IOUtils.toInputStream(FILE_CONTENT), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verifyWriteAppendByteArray() {
        try {
            getConnector().fileWrite(FILENAME, null, true, FILE_CONTENT.getBytes(), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}