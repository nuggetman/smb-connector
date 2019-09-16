/**
 * Copyright 2018-2019 (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.smb.automation.functional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.DIR_CREATE_DOUBLE_TEST_NAME;

import org.junit.After;
import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class DirectoryCreateDoubleTest extends AbstractTestCase<SmbConnector> {

    public DirectoryCreateDoubleTest() {
        super(SmbConnector.class);
    }

    @After
    public void tearDown() {
        try {
            getConnector().directoryDelete(DIR_CREATE_DOUBLE_TEST_NAME, true);
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verifyCreateDirectoryTwice() {
        try {
        	    assertTrue(getConnector().directoryCreate(DIR_CREATE_DOUBLE_TEST_NAME));
        	    assertFalse(getConnector().directoryCreate(DIR_CREATE_DOUBLE_TEST_NAME));
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}