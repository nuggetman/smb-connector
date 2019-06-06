package org.mule.modules.smb.automation.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.modules.smb.config.SmbConnectorConfig;
import org.mule.tools.devkit.ctf.configuration.util.ConfigurationUtils;

import jcifs.smb.NtlmPasswordAuthentication;

public class SmbValidConnectivityTest {

    private Properties validCredentials;
    private String domain;
    private String host;
    private String path;
    private String username;
    private String password;
    private String connectionTimeout;
    private String fileAge;
    private SmbConnectorConfig config;

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
        config = new SmbConnectorConfig();
        config.connect(domain, host, path, username, password, connectionTimeout, fileAge);
    }

    @Test
    public void validConnectivityTest() throws ConnectionException {
        assertTrue(config.getSmbClient().isConnected());
    }

    @Test
    public void validCredentialsConnectivityTest() throws ConnectionException {
        assertTrue(assertCredentials(config.getSmbClient().getCredentials()));
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
        config.setTimeout(Integer.parseInt(connectionTimeout));
        assertTrue(Integer.parseInt(connectionTimeout) == config.getTimeout());
    }
    
    @Test
    public void fileageTest() {
        config.setFileage(Integer.parseInt(fileAge));
        assertTrue(Integer.parseInt(fileAge) == config.getFileage());
    }

}