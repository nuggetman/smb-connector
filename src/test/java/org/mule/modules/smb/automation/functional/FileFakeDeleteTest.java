/**
 * Copyright 2018-2019 (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.smb.automation.functional;

import static org.junit.Assert.assertFalse;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.FILE_FAKEDELETE_TEST_FILENAME;

import org.junit.Test;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class FileFakeDeleteTest extends AbstractTestCase<SmbConnector> {

    public FileFakeDeleteTest() {
        super(SmbConnector.class);
    }

    @Test
    public void verify() {
        try {
            assertFalse(getConnector().fileDelete(FILE_FAKEDELETE_TEST_FILENAME, null));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}