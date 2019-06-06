package org.mule.modules.smb.automation.system;

import static org.junit.Assert.assertFalse;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.modules.smb.config.SmbConnectorConfig;
import org.mule.tools.devkit.ctf.configuration.util.ConfigurationUtils;

public class SmbInvalidConnectivityTest {

    private Properties validCredentials;
    private String domain;
    private String host;
    private String path;
    private String username;
    private String password;
    private String connectionTimeout;
    private String fileAge;

    @Before
    public void setUp() throws Exception {
    		System.setProperty("automation-credential.properties", "automation-credentials.properties");
        validCredentials = ConfigurationUtils.getAutomationCredentialsProperties();
        domain = validCredentials.getProperty("config.domain");
        host = validCredentials.getProperty("config.host");
        path = validCredentials.getProperty("config.path");
        username = validCredentials.getProperty("config.username");
        password = validCredentials.getProperty("config.password");
        connectionTimeout = validCredentials.getProperty("config.connectionTimeout");
        fileAge = validCredentials.getProperty("config.fileAge");
    }

    @Test(expected = ConnectionException.class)
    public void invalidPropertiesConnectivityTest() throws ConnectionException {
        String invalidField = "invalid-field";
        SmbConnectorConfig c = new SmbConnectorConfig();
        c.connect(invalidField, invalidField, invalidField, invalidField, invalidField, invalidField, invalidField);
        assertFalse(c.getSmbClient().isConnected());
    }
    
    // Unreliable test - linux SMB services can ignore domain
    /* @Test
    public void invalidDomainConnectivityTest() throws ConnectionException {
        String invalidField = "invalid-field";
        SmbConnectorConfig c = new SmbConnectorConfig();
        c.connect(invalidField, host, path, username, password, connectionTimeout, fileAge);
        assertTrue(c.getSmbClient().isConnected());
    } */
    
    @Test
    (expected = ConnectionException.class)
    public void invalidHostConnectivityTest() throws ConnectionException {
        String invalidField = "invalid-field";
        SmbConnectorConfig c = new SmbConnectorConfig();
        c.connect(domain, invalidField, path, username, password, connectionTimeout, fileAge);
        assertFalse(c.getSmbClient().isConnected());
    }
    
    @Test(expected = ConnectionException.class)
    public void invalidUsernameConnectivityTest() throws ConnectionException {
        String invalidField = "invalid-field";
        SmbConnectorConfig c = new SmbConnectorConfig();
        c.connect(domain, host, path, invalidField, password, connectionTimeout, fileAge);
        assertFalse(c.getSmbClient().isConnected());
    }
    
    @Test(expected = ConnectionException.class)
    public void invalidPasswordConnectivityTest() throws ConnectionException {
        String invalidField = "invalid-field";
        SmbConnectorConfig c = new SmbConnectorConfig();
        c.connect(domain, host, path, username, invalidField, connectionTimeout, fileAge);
        assertFalse(c.getSmbClient().isConnected());
    }
    
    @Test
    (expected = java.lang.AssertionError.class)
    public void invalidTimeoutConnectivityTest() throws ConnectionException {
        String invalidField = "invalid-field";
        SmbConnectorConfig c = new SmbConnectorConfig();
        c.connect(domain, host, path, username, password, invalidField, fileAge);
        assertFalse(c.getSmbClient().isConnected());
    }
    
    @Test
    (expected = java.lang.AssertionError.class)
    public void invalidFileageConnectivityTest() throws ConnectionException {
        String invalidField = "invalid-field";
        SmbConnectorConfig c = new SmbConnectorConfig();
        c.connect(domain, host, path, username, password, connectionTimeout, invalidField);
        assertFalse(c.getSmbClient().isConnected());
    }
    

}