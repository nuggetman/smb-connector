/**
 * Copyright 2018-2020 (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
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
import org.mule.modules.smb.automation.functional.DirectoryCreateTestCases;
import org.mule.modules.smb.automation.functional.DirectoryDeleteTestCases;
import org.mule.modules.smb.automation.functional.DirectoryListTestCases;
import org.mule.modules.smb.automation.functional.FileDeleteTestCases;
import org.mule.modules.smb.automation.functional.FileReadTestCases;
import org.mule.modules.smb.automation.functional.FileWriteTestCases;
import org.mule.tools.devkit.ctf.mockup.ConnectorTestContext;

@RunWith(Suite.class)
@SuiteClasses({ FileReadTestCases.class, FileWriteTestCases.class, FileDeleteTestCases.class, DirectoryCreateTestCases.class,
        DirectoryListTestCases.class, DirectoryDeleteTestCases.class })

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