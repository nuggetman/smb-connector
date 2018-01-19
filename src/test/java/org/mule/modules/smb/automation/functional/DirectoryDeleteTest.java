package org.mule.modules.smb.automation.functional;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class DirectoryDeleteTest extends AbstractTestCase<SmbConnector> {

    private String dirName = new String("testdeletefolder");

    public DirectoryDeleteTest() {
        super(SmbConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().directoryCreate(dirName);
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verify() {
        try {
            getConnector().directoryDelete(dirName);
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}