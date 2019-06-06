package org.mule.modules.smb.automation.system;

import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.modules.smb.config.SmbConnectorConfig;
import org.mule.tools.devkit.ctf.configuration.util.ConfigurationUtils;

public class SmbAnonymousConnectivityTest {

    private static Properties validCredentials;
    private static String host;
    private static String path;
    private static String timeout;
    private static String fileage;
    private static SmbConnectorConfig config;

    @BeforeClass
    public static void setUp() throws Exception {
        validCredentials = ConfigurationUtils.getAutomationCredentialsProperties();
        host = validCredentials.getProperty("config.anonymous.host");
        path = validCredentials.getProperty("config.anonymous.path");
        timeout = validCredentials.getProperty("config.anonymous.timeout");
        fileage = validCredentials.getProperty("config.anonymous.fileage");
        config = new SmbConnectorConfig();
        config.connect(host, path, null, null, null, timeout, fileage);
    }

    @Test
    public void validAnonymousConnectivityTest() throws ConnectionException {
        assertTrue(config.getSmbClient().isConnected());
    }

}
