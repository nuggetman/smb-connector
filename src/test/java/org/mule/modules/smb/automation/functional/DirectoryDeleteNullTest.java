package org.mule.modules.smb.automation.functional;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class DirectoryDeleteNullTest extends AbstractTestCase<SmbConnector> {

    public DirectoryDeleteNullTest() {
        super(SmbConnector.class);
    }

    @Test
    public void verifyNull() {
        try {
            assertFalse(getConnector().directoryDelete(null, true));
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    @Test
    public void verifyNoExist() {
        try {
            assertFalse(getConnector().directoryDelete("FAKE", true));
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}