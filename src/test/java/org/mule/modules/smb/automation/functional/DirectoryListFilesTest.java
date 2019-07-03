package org.mule.modules.smb.automation.functional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.mule.modules.smb.automation.functional.TestDataBuilder.DIR_NAME;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.FILENAME;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.FILE_CONTENT;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.WILDCARD;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.TXTWILDCARD;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.BAKWILDCARD;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class DirectoryListFilesTest extends AbstractTestCase<SmbConnector> {
    
    public DirectoryListFilesTest() {
        super(SmbConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().directoryCreate(DIR_NAME);
            getConnector().fileWrite(FILENAME, DIR_NAME, false, FILE_CONTENT, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @After
    public void tearDown() {
        try {
            getConnector().directoryDelete(DIR_NAME, true);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verifyDirListWithWildCard() {
        try {
        		assertFalse(getConnector().directoryList(DIR_NAME, WILDCARD).isEmpty());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    @Test
    public void verifyDirListWithTxtWildCard() {
        try {
        		assertFalse(getConnector().directoryList(DIR_NAME, TXTWILDCARD).isEmpty());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verifyDirListWithBakWildCard() {
        try {
        		assertTrue(getConnector().directoryList(DIR_NAME, BAKWILDCARD).isEmpty());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    @Test
    public void verifyDirListWithNoWildCard() {
        try {
        		assertFalse(getConnector().directoryList(DIR_NAME, null).isEmpty());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}