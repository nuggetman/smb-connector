/**
 * Copyright 2018-2020 (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.smb.automation.functional;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.DEFAULT_ENCODING;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.FILE_CONTENT;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.FILE_WRITE_TEST_FILENAME;

import org.junit.After;
import org.junit.Test;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class FileWriteTestCases extends AbstractTestCase<SmbConnector> {

    public FileWriteTestCases() {
        super(SmbConnector.class);
    }

    @After
    public void tearDown() {
        try {
            getConnector().fileDelete(FILE_WRITE_TEST_FILENAME, null);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verifyWriteNoAppend() {
        try {
            assertTrue(getConnector().fileWrite(FILE_WRITE_TEST_FILENAME, null, false, FILE_CONTENT, DEFAULT_ENCODING));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verifyFileWriteNullFail() {
        try {
            getConnector().fileWrite(null, null, false, FILE_CONTENT, DEFAULT_ENCODING);
            fail("Expected an Exception to be thrown");
        } catch (org.mule.api.ConnectionException connectionException) {
            assertTrue(connectionException.getMessage().startsWith("STATUS_FILE_IS_A_DIRECTORY (0xc00000ba)"));
        }
    }
}