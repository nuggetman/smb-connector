/**
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.smb.automation.functional;

import static org.mule.modules.smb.automation.functional.TestDataBuilder.FILE_WRITE_APPEND_STRING_TEST_FILENAME;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.FILE_CONTENT;

import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class FileWriteAppendStringTest extends AbstractTestCase<SmbConnector> {

    public FileWriteAppendStringTest() {
        super(SmbConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().fileWrite(FILE_WRITE_APPEND_STRING_TEST_FILENAME, null, false, FILE_CONTENT.toString(), "UTF-8");
            getConnector().fileWrite(FILE_WRITE_APPEND_STRING_TEST_FILENAME, null, true, FILE_CONTENT.toString(), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    @After
    public void tearDown() {
        try {
            getConnector().fileDelete(FILE_WRITE_APPEND_STRING_TEST_FILENAME, null);
        } catch ( Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verifyWriteAppendString() {
        try {
            assertEquals(FILE_CONTENT+FILE_CONTENT, new String(getConnector().fileRead(FILE_WRITE_APPEND_STRING_TEST_FILENAME, null, false)));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}