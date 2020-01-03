/**
 * Copyright 2018-2020 (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.smb.automation.functional;

import static org.junit.Assert.assertFalse;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.DIR_DELETE_FAKE_NAME;

import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class DirectoryDeleteNullTest extends AbstractTestCase<SmbConnector> {

    public DirectoryDeleteNullTest() {
        super(SmbConnector.class);
    }

    @Test
    public void verifyNull() {
        try {
            assertFalse(getConnector().directoryDelete(null, true));
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    @Test
    public void verifyNoExist() {
        try {
            assertFalse(getConnector().directoryDelete(DIR_DELETE_FAKE_NAME, true));
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}