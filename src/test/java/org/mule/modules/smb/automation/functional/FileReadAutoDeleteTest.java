package org.mule.modules.smb.automation.functional;


import static org.junit.Assert.assertEquals;

import static org.mule.modules.smb.automation.functional.TestDataBuilder.FILENAME;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.FILE_CONTENT;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class FileReadAutoDeleteTest extends AbstractTestCase<SmbConnector> {

    public FileReadAutoDeleteTest() {
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
    }
    
    @Test
    public void verifyFileReadWithDelete() {
        try {
            assertEquals(FILE_CONTENT, new String(getConnector().fileRead(FILENAME, null, true)));
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}