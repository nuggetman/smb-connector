/**
 * Copyright 2018-2020 (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.smb.automation.functional;

import static org.junit.Assert.assertTrue;

import static org.mule.modules.smb.automation.functional.TestDataBuilder.DIR_LIST_FILES_BAK_TEST_NAME;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.DIR_LIST_FILES_BAK_TEST_FILE_NAME;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.FILE_CONTENT;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.BAKWILDCARD;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class DirectoryListFilesWildcardBakTest extends AbstractTestCase<SmbConnector> {
    
    public DirectoryListFilesWildcardBakTest() {
        super(SmbConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().directoryCreate(DIR_LIST_FILES_BAK_TEST_NAME);
            getConnector().fileWrite(DIR_LIST_FILES_BAK_TEST_FILE_NAME, DIR_LIST_FILES_BAK_TEST_NAME, false, FILE_CONTENT, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @After
    public void tearDown() {
        try {
            getConnector().directoryDelete(DIR_LIST_FILES_BAK_TEST_NAME, true);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    @Test
    public void verifyDirListWithBakWildCard() {
        try {
        		assertTrue(getConnector().directoryList(DIR_LIST_FILES_BAK_TEST_NAME, BAKWILDCARD).isEmpty());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}