/**
 * Copyright 2018-2020 (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.smb.automation.functional;

import static org.junit.Assert.assertTrue;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.DIR_LIST_EMPTY_TEST_NAME;

import org.junit.Test;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class DirectoryAnonListEmptyTestCases extends AbstractTestCase<SmbConnector> {

    public DirectoryAnonListEmptyTestCases() {
        super(SmbConnector.class);
    }

    @Test
    public void verifyDirListWithNoWildCard() {
        try {
            assertTrue(getConnector().directoryList(DIR_LIST_EMPTY_TEST_NAME, null).isEmpty());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}