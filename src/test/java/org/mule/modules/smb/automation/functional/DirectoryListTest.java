package org.mule.modules.smb.automation.functional;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class DirectoryListTest extends AbstractTestCase<SmbConnector> {

    private String dirName = new String("testlistfolder");
    private String wildcard = new String("*");

    public DirectoryListTest() {
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

    @After
    public void tearDown() {
        try {
            getConnector().directoryDelete(dirName);
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verifyDirListWithWildCard() {
        try {
            assertNotNull(getConnector().directoryList(dirName, wildcard));
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verifyDirListWithNoWildCard() {
        try {
            assertNotNull(getConnector().directoryList(dirName, null));
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}