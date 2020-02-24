/**
 * Copyright 2018-2020 (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.smb.automation.functional;


import static org.junit.Assert.assertEquals;

import static org.mule.modules.smb.automation.functional.TestDataBuilder.FILE_READ_AUTODELETE_TEST_FILENAME;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.FILE_CONTENT;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class FileReadAutoDeleteTestCases extends AbstractTestCase<SmbConnector> {

    public FileReadAutoDeleteTestCases() {
        super(SmbConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().fileWrite(FILE_READ_AUTODELETE_TEST_FILENAME, null, false, FILE_CONTENT.getBytes(), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @After
    public void tearDown() {
    }
    
    @Test
    public void verifyFileReadWithDelete() {
        try {
            assertEquals(FILE_CONTENT, new String(getConnector().fileRead(FILE_READ_AUTODELETE_TEST_FILENAME, null, true)));
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}