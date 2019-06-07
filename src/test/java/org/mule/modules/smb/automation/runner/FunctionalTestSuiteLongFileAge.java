package org.mule.modules.smb.automation.runner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.mule.modules.smb.SmbConnector;
import org.mule.modules.smb.automation.functional.DirectoryListFilesLongTest;
import org.mule.modules.smb.automation.functional.FileDeleteLongTest;
import org.mule.modules.smb.automation.functional.FileReadLongTest;
import org.mule.tools.devkit.ctf.mockup.ConnectorTestContext;

@RunWith(Suite.class)
@SuiteClasses({
    FileReadLongTest.class,
    FileDeleteLongTest.class,
    DirectoryListFilesLongTest.class
})

public class FunctionalTestSuiteLongFileAge {

    @BeforeClass
    public static void initialiseSuite() {
    		System.setProperty("automation-credentials.properties", "automation-credentials-long-fileage.properties");
        ConnectorTestContext.initialize(SmbConnector.class);
    }

    @AfterClass
    public static void shutdownSuite() {
        ConnectorTestContext.shutDown();
    }

}