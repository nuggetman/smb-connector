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
import org.mule.modules.smb.automation.functional.DirectoryCreateDoubleTestCases;
import org.mule.modules.smb.automation.functional.DirectoryCreateNullTestCases;
import org.mule.modules.smb.automation.functional.DirectoryCreateTestCases;
import org.mule.modules.smb.automation.functional.DirectoryDeleteTestCases;
import org.mule.modules.smb.automation.functional.DirectoryListTestCases;
import org.mule.modules.smb.automation.functional.DirectoryListFilesWildcardBakTestCases;
import org.mule.modules.smb.automation.functional.DirectoryListFilesWildcardTestCases;
import org.mule.modules.smb.automation.functional.DirectoryListFilesWildcardTxtTestCases;
import org.mule.modules.smb.automation.functional.DirectoryListTestCases;
import org.mule.modules.smb.automation.functional.FileDeleteTestCases;
import org.mule.modules.smb.automation.functional.FileFakeDeleteTestCases;
import org.mule.modules.smb.automation.functional.FileReadAutoDeleteTestCases;
import org.mule.modules.smb.automation.functional.FileReadTestCases;
import org.mule.modules.smb.automation.functional.FileWriteAppendByteArrayTestCases;
import org.mule.modules.smb.automation.functional.FileWriteAppendInputStreamTestCases;
import org.mule.modules.smb.automation.functional.FileWriteAppendStringTestCases;
import org.mule.modules.smb.automation.functional.FileWriteTestCases;
import org.mule.tools.devkit.ctf.mockup.ConnectorTestContext;

@RunWith(Suite.class)
@SuiteClasses({ FileReadTestCases.class, FileReadAutoDeleteTestCases.class, FileWriteTestCases.class,
        FileWriteAppendStringTestCases.class, FileWriteAppendByteArrayTestCases.class,
        FileWriteAppendInputStreamTestCases.class, FileDeleteTestCases.class, FileFakeDeleteTestCases.class,
        DirectoryCreateTestCases.class, DirectoryCreateDoubleTestCases.class, DirectoryCreateNullTestCases.class,
        DirectoryListTestCases.class, DirectoryListTestCases.class, DirectoryListFilesWildcardTestCases.class,
        DirectoryListFilesWildcardBakTestCases.class, DirectoryListFilesWildcardTxtTestCases.class,
        DirectoryDeleteTestCases.class,

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