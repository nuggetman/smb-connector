package org.mule.modules.smb.automation.system;

import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.smb.config.SmbConnectorConfig;
import org.mule.modules.smb.exception.SmbConnectionException;
import org.mule.tools.devkit.ctf.configuration.util.ConfigurationUtils;

public class SmbReadOnlyTest {

    private Properties credentials;

    private SmbConnectorConfig config;
    private String fileName = new String("testdeletefile.txt");
    private String fileContent = new String("somecontent");

    @Before
    public void setUp() throws Exception {
        credentials = ConfigurationUtils.getAutomationCredentialsProperties();
        config = new SmbConnectorConfig();

        // connect with RW credentials
        config.connect(credentials.getProperty("config.domain"), credentials.getProperty("config.host"), credentials.getProperty("config.path"),
                credentials.getProperty("config.username"), credentials.getProperty("config.password"), credentials.getProperty("config.timeout"));

        // write out a test file
        config.getSmbClient().writeFile(fileName, true, fileContent.getBytes(), "UTF-8");

        // reconnect with RO credentials
        config.connect(credentials.getProperty("config.readonly.domain"), credentials.getProperty("config.readonly.host"), credentials.getProperty("config.readonly.path"),
                credentials.getProperty("config.readonly.username"), credentials.getProperty("config.readonly.password"), credentials.getProperty("config.readonly.timeout"));
    }

    @After
    public void tearDown() throws Exception {

        // connect with RW credentials
        config.connect(credentials.getProperty("config.domain"), credentials.getProperty("config.host"), credentials.getProperty("config.path"),
                credentials.getProperty("config.username"), credentials.getProperty("config.password"), credentials.getProperty("config.timeout"));

        // clean up test file
        // config.getSmbClient().deleteFile(fileName);
    }

    @Test
    public void validCredentialsConnectivityTest() throws SmbConnectionException {
        assertTrue(config.getSmbClient().isConnected());
    }

    @Test(expected = SmbConnectionException.class)
    public void invalidWriteTest() throws SmbConnectionException {
        config.getSmbClient().writeFile(fileName, true, fileContent.getBytes(), "UTF-8");
    }

    @Test(expected = SmbConnectionException.class)
    public void invalidDeleteTest() throws SmbConnectionException {
        config.getSmbClient().deleteFile(fileName);
    }

}
