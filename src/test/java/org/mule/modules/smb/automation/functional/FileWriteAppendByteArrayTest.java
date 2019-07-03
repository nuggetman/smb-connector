package org.mule.modules.smb.automation.functional;

import static org.mule.modules.smb.automation.functional.TestDataBuilder.FILE_WRITE_APPEND_BYTEARRAY_TEST_FILENAME;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.FILE_CONTENT;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class FileWriteAppendByteArrayTest extends AbstractTestCase<SmbConnector> {

    public FileWriteAppendByteArrayTest() {
        super(SmbConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().fileWrite(FILE_WRITE_APPEND_BYTEARRAY_TEST_FILENAME, null, false, FILE_CONTENT.getBytes(), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    @After
    public void tearDown() {
        try {
            getConnector().fileDelete(FILE_WRITE_APPEND_BYTEARRAY_TEST_FILENAME, null);
        } catch ( Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verifyWriteAppendByteArray() {
        try {
            getConnector().fileWrite(FILE_WRITE_APPEND_BYTEARRAY_TEST_FILENAME, null, true, FILE_CONTENT.getBytes(), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}