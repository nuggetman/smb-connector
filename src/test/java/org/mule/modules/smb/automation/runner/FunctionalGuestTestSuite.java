/**
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
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
    DirectoryDeleteTest.class
})

public class FunctionalGuestTestSuite {

    @BeforeClass
    public static void initialiseSuite() {
    		System.setProperty("automation-credentials.properties", "automation-credentials-guest.properties");
        ConnectorTestContext.initialize(SmbConnector.class);
    }

    @AfterClass
    public static void shutdownSuite() {
        ConnectorTestContext.shutDown();
    }

}