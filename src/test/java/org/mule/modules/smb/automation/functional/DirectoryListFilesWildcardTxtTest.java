package org.mule.modules.smb.automation.functional;

import static org.junit.Assert.assertFalse;

import static org.mule.modules.smb.automation.functional.TestDataBuilder.DIR_LIST_FILES_TXT_TEST_NAME;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.DIR_LIST_FILES_TXT_TEST_FILE_NAME;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.FILE_CONTENT;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.TXTWILDCARD;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class DirectoryListFilesWildcardTxtTest extends AbstractTestCase<SmbConnector> {
    
    public DirectoryListFilesWildcardTxtTest() {
        super(SmbConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().directoryCreate(DIR_LIST_FILES_TXT_TEST_NAME);
            getConnector().fileWrite(DIR_LIST_FILES_TXT_TEST_FILE_NAME, DIR_LIST_FILES_TXT_TEST_NAME, false, FILE_CONTENT, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @After
    public void tearDown() {
        try {
            getConnector().directoryDelete(DIR_LIST_FILES_TXT_TEST_NAME, true);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    @Test
    public void verifyDirListWithTxtWildCard() {
        try {
        		assertFalse(getConnector().directoryList(DIR_LIST_FILES_TXT_TEST_NAME, TXTWILDCARD).isEmpty());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}