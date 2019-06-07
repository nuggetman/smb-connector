package org.mule.modules.smb.automation.functional;

import static org.junit.Assert.assertNull;

import static org.mule.modules.smb.automation.functional.TestDataBuilder.CANT_DELETE_FILENAME;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.CANT_DELETE_DIRNAME;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.FILE_CONTENT;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class FileReadLongTest extends AbstractTestCase<SmbConnector> {
	
	public FileReadLongTest() {
        super(SmbConnector.class);
    }

    @Before
    public void setup() {
        try {
        		getConnector().directoryCreate(CANT_DELETE_DIRNAME);
            getConnector().fileWrite(CANT_DELETE_FILENAME, CANT_DELETE_DIRNAME, false, FILE_CONTENT.getBytes(), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @After
    public void tearDown() {
        try {
        		getConnector().directoryDelete(CANT_DELETE_DIRNAME);
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    @Test
    public void verifyFileReadNoDelete() {
        try {
            assertNull(getConnector().fileRead(CANT_DELETE_FILENAME, CANT_DELETE_DIRNAME, false));
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    @Test
    public void verifyFileReadWithDelete() {
        try {
        		assertNull(getConnector().fileRead(CANT_DELETE_FILENAME, CANT_DELETE_DIRNAME, true));
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}