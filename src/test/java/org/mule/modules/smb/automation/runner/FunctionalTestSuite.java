package org.mule.modules.smb.automation.runner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.mule.modules.smb.automation.functional.DirectoryCreateTestCases;
import org.mule.modules.smb.automation.functional.DirectoryDeleteTestCases;
import org.mule.modules.smb.automation.functional.DirectoryListTestCases;
import org.mule.modules.smb.automation.functional.FileDeleteTestCases;
import org.mule.modules.smb.automation.functional.FileReadTestCases;
import org.mule.modules.smb.automation.functional.FileWriteTestCases;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.mockup.ConnectorTestContext;

@RunWith(Suite.class)
@SuiteClasses({
    DirectoryCreateTestCases.class,
    DirectoryDeleteTestCases.class,
    DirectoryListTestCases.class,
    FileDeleteTestCases.class,
    FileReadTestCases.class,
    FileWriteTestCases.class
})

public class FunctionalTestSuite {

    @BeforeClass
    public static void initialiseSuite() {
        ConnectorTestContext.initialize(SmbConnector.class);
    }

    @AfterClass
    public static void shutdownSuite() {
        ConnectorTestContext.shutDown();
    }

}