package org.mule.modules.smb.automation.functional;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class DirectoryCreateNullTest extends AbstractTestCase<SmbConnector> {

    public DirectoryCreateNullTest() {
        super(SmbConnector.class);
    }

    @Test
    public void verifyCreateDirectory() {
        try {
            assertFalse(getConnector().directoryCreate(null));
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}