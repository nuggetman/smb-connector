package org.mule.modules.smb.automation.runner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.mule.modules.smb.SmbConnector;
import org.mule.modules.smb.automation.functional.DirectoryCreateTest;
import org.mule.modules.smb.automation.functional.DirectoryDeleteTest;
import org.mule.modules.smb.automation.functional.DirectoryListEmptyTest;
import org.mule.modules.smb.automation.functional.DirectoryListFilesTest;
import org.mule.modules.smb.automation.functional.FileDeleteTest;
import org.mule.modules.smb.automation.functional.FileReadTest;
import org.mule.modules.smb.automation.functional.FileWriteTest;
import org.mule.tools.devkit.ctf.mockup.ConnectorTestContext;

@RunWith(Suite.class)
@SuiteClasses({
    FileReadTest.class,
    FileWriteTest.class,
    FileDeleteTest.class,
    DirectoryCreateTest.class,
    DirectoryListEmptyTest.class,
    DirectoryListFilesTest.class,
    DirectoryDeleteTest.class
})

public class FunctionalTestSuiteAnonymous {

    @BeforeClass
    public static void initialiseSuite() {
    		System.setProperty("automation-credentials.properties", "automation-credentials-anonymous.properties");
        ConnectorTestContext.initialize(SmbConnector.class);
    }

    @AfterClass
    public static void shutdownSuite() {
        ConnectorTestContext.shutDown();
    }

}