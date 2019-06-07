package org.mule.modules.smb.automation.functional;

import static org.junit.Assert.assertTrue;

import static org.mule.modules.smb.automation.functional.TestDataBuilder.DIR_NAME;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.WILDCARD;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class DirectoryListEmptyTest extends AbstractTestCase<SmbConnector> {

    public DirectoryListEmptyTest() {
        super(SmbConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().directoryCreate(DIR_NAME);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @After
    public void tearDown() {
        try {
            getConnector().directoryDelete(DIR_NAME);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verifyDirListWithWildCard() {
        try {
        		assertTrue(getConnector().directoryList(DIR_NAME, WILDCARD).isEmpty());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verifyDirListWithNoWildCard() {
        try {
        		assertTrue(getConnector().directoryList(DIR_NAME, null).isEmpty());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}