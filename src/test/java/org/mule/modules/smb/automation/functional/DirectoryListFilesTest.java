package org.mule.modules.smb.automation.functional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class DirectoryListFilesTest extends AbstractTestCase<SmbConnector> {

    private String dirName = new String("testlistfolder");
    private String wildCard = new String("*.*");
    private String txtwildCard = new String("*.txt");
    private String bakwildCard = new String("*.bak");
    private String fileName = new String("somefile.txt");
    private String fileContent = new String("some content");

    public DirectoryListFilesTest() {
        super(SmbConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().directoryCreate(dirName);
            getConnector().fileWrite(fileName, dirName, false, fileContent, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @After
    public void tearDown() {
        try {
            getConnector().directoryDelete(dirName);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verifyDirListWithWildCard() {
        try {
        		assertFalse(getConnector().directoryList(dirName, wildCard).isEmpty());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    @Test
    public void verifyDirListWithTxtWildCard() {
        try {
        		assertFalse(getConnector().directoryList(dirName, txtwildCard).isEmpty());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verifyDirListWithBakWildCard() {
        try {
        		assertTrue(getConnector().directoryList(dirName, bakwildCard).isEmpty());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    @Test
    public void verifyDirListWithNoWildCard() {
        try {
        		assertFalse(getConnector().directoryList(dirName, null).isEmpty());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}