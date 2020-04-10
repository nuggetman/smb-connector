/**
 * Copyright 2018-2020 (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.smb.config;

import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.annotations.Connect;
import org.mule.api.annotations.ConnectionIdentifier;
import org.mule.api.annotations.Disconnect;
import org.mule.api.annotations.TestConnectivity;
import org.mule.api.annotations.ValidateConnection;
import org.mule.api.annotations.components.ConnectionManagement;
import org.mule.api.annotations.display.FriendlyName;
import org.mule.api.annotations.display.Password;
import org.mule.api.annotations.param.ConnectionKey;
import org.mule.api.annotations.param.Optional;
import org.mule.modules.smb.internal.SmbClient;
import org.mule.modules.smb.utils.Utilities;
import org.mule.util.NumberUtils;


@ConnectionManagement(friendlyName = "Configuration", configElementName = "config")
public class SmbConnectorConfig {

    private SmbClient smbClient = new SmbClient(this);

    private String domain;

    private String username;

    private String password;

    private String host;

    private String share;

    private int connectionTimeout = 30000;
    
    private int fileage = 500;
    
    private boolean guest = false;
    
    private boolean anonymous = false;

    /**
     * Set the host for the config
     *
     * @param host
     *            Host name to set
     * 
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Get the host from the config
     *
     * @return The host as a String
     */
    public String getHost() {
        return this.host;
    }

    /**
     * Set the share for the config
     *
     * @param share, share name
     */
    public void setShare(String share) {
        this.share = share;
    }

    /**
     * Get the share from the config
     *
     * @return The path as a String
     */
    public String getShare() {
        return this.share;
    }

    /**
     * Set the connect timeout (ms) for the config
     *
     * @param t, timeout value in ms
     */
    public void setTimeout(int t) {
        this.connectionTimeout = t;
    }

    /**
     * Get the connect timeout (ms) from the config
     *
     * @return timeout as an int
     */
    public int getTimeout() {
        return this.connectionTimeout;
    }

    /**
     * Set the domain name for the config
     *
     * @param d, domain name
     */
    public void setDomain(String d) {
        this.domain = d;
    }

    /**
     * Get the domain name from the config
     *
     * @return A string of the domain name
     */
    public String getDomain() {
        return this.domain;
    }

    /**
     * Set the user name for the config
     *
     * @param u,  username value
     */
    public void setUsername(String u) {
        this.username = u;
    }

    /**
     * Get the user name from the config
     *
     * @return A string of the user name
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Set the password for the config
     *
     * @param p, password value
     */
    public void setPassword(String p) {
        this.password = p;
    }

    /**
     * Get the password from the config
     *
     * @return A string of the password
     */
    public String getPassword() {
        return this.password;
    }
    
    /**
     * Set the file age limit (ms) for the config
     *
     * @param a, file age in ms
     */
    public void setFileage(int a) {
        this.fileage = a;
    }

    /**
     * Get the file age limit from the config
     *
     * @return An int of the file age limit
     */
    public int getFileage() {
        return this.fileage;
    }
    
    /**
     * Set guest credentials
     *
     * @param g, if true use guest credentials
     */
    public void setGuest(boolean g) {
        this.guest = g;
    }

    /**
     * Get the guest credential status
     *
     * @return boolean, true if guest enabled
     */
    public boolean getGuest() {
        return this.guest;
    }

    /**
     * Set anonymous credentials
     *
     * @param a, if true use anonymous credentials
     * 
     */
    public void setAnonymous(boolean a) {
        this.anonymous = a;
    }

    /**
     * Get the anonymous credential status
     *
     * @return boolean, true if anonymous enabled
     */
    public boolean getAnonymous() {
        return this.anonymous;
    }

    /**
     * Get the client that does the work
     *
     * @return SmbClient instance
     */
    public SmbClient getSmbClient() {
        return smbClient;
    }

    /**
     * Connect
     *
     * @param domain, domain name
	 * @param host, host name to connect
     * @param share, share name
     * @param username, username for connection
     * @param password, password for connection
     * @param timeout, connection timeout
     * @param fileage, file age for actions
     * @throws ConnectionException if there is a connection issue
     */
    @Connect
    @TestConnectivity
    public void connect(@ConnectionKey @Optional @FriendlyName("Domain") String domain,
    		@ConnectionKey @FriendlyName("Host") String host,
        @ConnectionKey @Optional @FriendlyName("Share") String share,
        @Optional @FriendlyName("Username") String username,
        @Optional @Password @FriendlyName("Password") String password, 
        @Optional @FriendlyName("Connection timeout") String timeout,
        @Optional @FriendlyName("File age (ms)") String fileage) throws ConnectionException {

        try {
        	
            this.setDomain(domain);
            if (username.equalsIgnoreCase("guest")) {
                this.setGuest(true);
            } else if (username.equalsIgnoreCase("anonymous")) {
                this.setAnonymous(true);
            } else {
                this.setUsername(username);
                this.setPassword(password);
            }
            this.setHost(host);
            this.setShare(Utilities.cleanPath(share));
            if (NumberUtils.isNumber(timeout)) {
                this.setTimeout(Integer.parseInt(timeout));
            }
            if (NumberUtils.isNumber(fileage)) {
                this.setFileage(Integer.parseInt(fileage));
            }
            this.getSmbClient().connect();
        } catch (Exception e) {
            throw new org.mule.api.ConnectionException(ConnectionExceptionCode.UNKNOWN, null, e.getMessage(), e);
        }
    }

    /**
     * Disconnect
     */
    @Disconnect
    public void disconnect() {
        this.getSmbClient().disconnect();
    }

    /**
     * Are we connected
     * 
     * @return boolean, true if connected
     */
    @ValidateConnection
    public boolean isConnected() {
        return this.getSmbClient().isConnected();
    }

    /**
     * get connection Id
     * 
     * @return String, client connection id
     */
    @ConnectionIdentifier
    public String connectionId() {
        return this.getSmbClient().connectionId();
    }

}