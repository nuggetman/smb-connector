/**
 * Copyright 2018-2020 (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.smb.automation.functional;

import static org.junit.Assert.assertTrue;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.DEFAULT_ENCODING;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.FILE_CONTENT;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.FILE_WRITE_APPEND_INPUTSTREAM_TEST_FILENAME;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class FileWriteAppendInputStreamTestCases extends AbstractTestCase<SmbConnector> {

    public FileWriteAppendInputStreamTestCases() {
        super(SmbConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().fileWrite(FILE_WRITE_APPEND_INPUTSTREAM_TEST_FILENAME, null, false, FILE_CONTENT.getBytes(),
                    DEFAULT_ENCODING);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @After
    public void tearDown() {
        try {
            getConnector().fileDelete(FILE_WRITE_APPEND_INPUTSTREAM_TEST_FILENAME, null);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verifyWriteAppendInputStream() {
        try {
            assertTrue(getConnector().fileWrite(FILE_WRITE_APPEND_INPUTSTREAM_TEST_FILENAME, null, true,
                    IOUtils.toInputStream(FILE_CONTENT), DEFAULT_ENCODING));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}