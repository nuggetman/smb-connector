/**
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.smb.automation.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.modules.smb.config.SmbConnectorConfig;
import org.mule.tools.devkit.ctf.configuration.util.ConfigurationUtils;

public class ZeroFileageConnectivityTest {

    private Properties validCredentials;
    private String domain;
    private String host;
    private String share;
    private String username;
    private String password;
    private String connectionTimeout;
    private String fileage;
    private SmbConnectorConfig config;

    @Before
    public void setUp() throws Exception {
    		System.setProperty("automation-credential.properties", "automation-credentials.properties");
        validCredentials = ConfigurationUtils.getAutomationCredentialsProperties();
        domain = validCredentials.getProperty("config.domain");
        host = validCredentials.getProperty("config.host");
        share = validCredentials.getProperty("config.share");
        username = validCredentials.getProperty("config.username");
        password = validCredentials.getProperty("config.password");
        connectionTimeout = "some string";
        fileage = "0";	
        config = new SmbConnectorConfig();
        config.connect(domain, host, share, username, password, connectionTimeout, fileage);
    }

    @Test
    public void validConnectivityTest() throws ConnectionException {
        assertTrue(config.getSmbClient().isConnected());
    }

    @Test
    public void connectionIdTest() {
        assertEquals("001", config.connectionId());
    }

    @Test
    public void timeoutTest() {
        assertTrue(config.getTimeout() == 30000);
    }
    
    @Test
    public void fileageTest() {
        assertTrue(config.getFileage() == 0);
    }

}