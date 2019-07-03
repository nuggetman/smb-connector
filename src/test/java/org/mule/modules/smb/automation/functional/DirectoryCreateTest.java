package org.mule.modules.smb.automation.functional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.DIR_NAME;

import org.junit.After;
import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class DirectoryCreateTest extends AbstractTestCase<SmbConnector> {

    public DirectoryCreateTest() {
        super(SmbConnector.class);
    }

    @After
    public void tearDown() {
        try {
            getConnector().directoryDelete(DIR_NAME, true);
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verifyCreateDirectory() {
        try {
            assertTrue(getConnector().directoryCreate(DIR_NAME));
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verifyCreateDirectoryTwice() {
        try {
        	    assertTrue(getConnector().directoryCreate(DIR_NAME));
        	    assertFalse(getConnector().directoryCreate(DIR_NAME));
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}