package org.mule.modules.smb.automation.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.modules.smb.config.SmbConnectorConfig;
import org.mule.tools.devkit.ctf.configuration.util.ConfigurationUtils;

import jcifs.smb.NtlmPasswordAuthentication;

public class SmbConnectivityTest {

    private Properties validCredentials;
    private String domain;
    private String host;
    private String path;
    private String username;
    private String password;
    private String timeout;
    private SmbConnectorConfig config;

    @Before
    public void setUp() throws Exception {
        validCredentials = ConfigurationUtils.getAutomationCredentialsProperties();
        domain = validCredentials.getProperty("config.domain");
        host = validCredentials.getProperty("config.host");
        path = validCredentials.getProperty("config.path");
        username = validCredentials.getProperty("config.username");
        password = validCredentials.getProperty("config.password");
        timeout = validCredentials.getProperty("config.timeout");
        config = new SmbConnectorConfig();
        config.connect(domain, host, path, username, password, timeout);
    }

    @Test
    public void validConnectivityTest() throws ConnectionException {
        assertTrue(config.getSmbClient().isConnected());
    }

    @Test(expected = ConnectionException.class)
    public void invalidCredentialsConnectivityTest() throws ConnectionException {
        String invalidField = "invalid-field";
        SmbConnectorConfig c = new SmbConnectorConfig();
        c.connect(invalidField, invalidField, invalidField, invalidField, invalidField, invalidField);
        assertFalse(c.getSmbClient().isConnected());
    }

    @Test
    public void validCredentialsConnectivityTest() throws ConnectionException {
        assertTrue(assertCredentials(config.getSmbClient().getCredentials()));
        assertTrue(config.getSmbClient().isConnected());
    }

    private boolean assertCredentials(NtlmPasswordAuthentication n) {
        return n.getDomain().equals(domain) && n.getUsername().equals(username) && n.getPassword().equals(password);
    }

    @Test
    public void connectionIdTest() {
        assertEquals("001", config.connectionId());
    }

    @Test
    public void timeoutTest() {
        config.setTimeout(Integer.parseInt(timeout));
        assertTrue(Integer.parseInt(timeout) == config.getTimeout());
    }

}
