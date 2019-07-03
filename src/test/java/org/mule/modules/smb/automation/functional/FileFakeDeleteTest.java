package org.mule.modules.smb.automation.functional;

import static org.junit.Assert.assertFalse;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.FILENAME;

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
            assertFalse(getConnector().fileDelete(FILENAME, null));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}