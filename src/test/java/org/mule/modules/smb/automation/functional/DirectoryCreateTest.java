package org.mule.modules.smb.automation.functional;

import org.junit.After;
import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class DirectoryCreateTest extends AbstractTestCase<SmbConnector> {

    private String dirName = new String("testcreatefolder");

    public DirectoryCreateTest() {
        super(SmbConnector.class);
    }

    @After
    public void tearDown() {
        try {
            getConnector().directoryDelete(dirName);
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verifyCreateDirectory() {
        try {
            getConnector().directoryCreate(dirName);
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verifyCreateDirectoryTwice() {
        try {
            getConnector().directoryCreate(dirName);
            getConnector().directoryCreate(dirName);
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}