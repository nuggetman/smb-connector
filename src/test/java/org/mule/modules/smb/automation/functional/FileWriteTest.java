package org.mule.modules.smb.automation.functional;

import static org.mule.modules.smb.automation.functional.TestDataBuilder.FILENAME;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.FILE_CONTENT;

import org.junit.After;
import org.junit.Test;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class FileWriteTest extends AbstractTestCase<SmbConnector> {

    public FileWriteTest() {
        super(SmbConnector.class);
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
    public void verifyWriteNoAppend() {
        try {
            getConnector().fileWrite(FILENAME, null, false, FILE_CONTENT, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}