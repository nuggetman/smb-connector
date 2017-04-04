package org.mule.modules.smb.automation.functional;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class DirectoryListTestCases extends AbstractTestCase<SmbConnector> {

    public DirectoryListTestCases() {
        super(SmbConnector.class);
    }

    @Before
    public void setup() {
        // TODO
    }

    @After
    public void tearDown() {
        // TODO
    }

    @Test
    public void verify() {
        java.util.List<java.util.Map<java.lang.String, java.lang.Object>> expected = null;
        java.lang.String dirName = null;
        java.lang.String wildcard = null;
        try {
            assertEquals(getConnector().directoryList(dirName, wildcard), expected);
        } catch (ConnectionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}