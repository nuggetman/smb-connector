package org.mule.modules.smb.automation.runner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.mule.modules.smb.SmbConnector;
import org.mule.modules.smb.automation.functional.DirectoryCreateDoubleTest;
import org.mule.modules.smb.automation.functional.DirectoryCreateNullTest;
import org.mule.modules.smb.automation.functional.DirectoryCreateTest;
import org.mule.modules.smb.automation.functional.DirectoryDeleteNullTest;
import org.mule.modules.smb.automation.functional.DirectoryDeleteTest;
import org.mule.modules.smb.automation.functional.DirectoryListEmptyTest;
import org.mule.modules.smb.automation.functional.DirectoryListEmptyWildcardTest;
import org.mule.modules.smb.automation.functional.DirectoryListFilesTest;
import org.mule.modules.smb.automation.functional.DirectoryListFilesWildcardBakTest;
import org.mule.modules.smb.automation.functional.DirectoryListFilesWildcardTest;
import org.mule.modules.smb.automation.functional.DirectoryListFilesWildcardTxtTest;
import org.mule.modules.smb.automation.functional.FileDeleteTest;
import org.mule.modules.smb.automation.functional.FileFakeDeleteTest;
import org.mule.modules.smb.automation.functional.FileReadAutoDeleteTest;
import org.mule.modules.smb.automation.functional.FileReadTest;
import org.mule.modules.smb.automation.functional.FileWriteAppendByteArrayTest;
import org.mule.modules.smb.automation.functional.FileWriteAppendInputStreamTest;
import org.mule.modules.smb.automation.functional.FileWriteAppendStringTest;
import org.mule.modules.smb.automation.functional.FileWriteTest;
import org.mule.tools.devkit.ctf.mockup.ConnectorTestContext;

@RunWith(Suite.class)
@SuiteClasses({
    FileReadTest.class,
    FileReadAutoDeleteTest.class,
    FileWriteTest.class,
    FileWriteAppendStringTest.class,
    FileWriteAppendByteArrayTest.class,
    FileWriteAppendInputStreamTest.class,
    FileDeleteTest.class,
    FileFakeDeleteTest.class,
    DirectoryCreateTest.class,
    DirectoryCreateDoubleTest.class,
    DirectoryCreateNullTest.class,
    DirectoryListEmptyTest.class,
    DirectoryListEmptyWildcardTest.class,
    DirectoryListFilesTest.class,
    DirectoryListFilesWildcardTest.class,
    DirectoryListFilesWildcardBakTest.class,
    DirectoryListFilesWildcardTxtTest.class,
    DirectoryDeleteTest.class,
    DirectoryDeleteNullTest.class,
   
})

public class FunctionalTestSuite {

    @BeforeClass
    public static void initialiseSuite() {
    		System.setProperty("automation-credentials.properties", "automation-credentials.properties");
        ConnectorTestContext.initialize(SmbConnector.class);
    }

    @AfterClass
    public static void shutdownSuite() {
        ConnectorTestContext.shutDown();
    }

}