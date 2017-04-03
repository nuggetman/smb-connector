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

@ConnectionManagement(friendlyName = "Configuration")
public class SMBConnectorConfig {

    // private static final Logger logger = LoggerFactory.getLogger(SMBConnectorConfig.class);

    private SmbClient smbClient = new SmbClient(this);

    private String domain;

    private String username;

    private String password;

    private String host;

    private String path;

    private int connectionTimeout = 30000;

    /**
     * Set the host for the config
     *
     * @param host
     *            Host name to set
     * @return void
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
     * Set the path for the config
     *
     * @param path
     *            Path name to set
     * @return void
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Get the path from the config
     *
     * @return The path as a String
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Set the connect timeout (ms) for the config
     *
     * @return void
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
     * @return void
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
     * @return void
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
     * @return void
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
     * @param domain
     *            domain
     * @param username
     *            username
     * @param password
     *            password
     * @param path
     *            shared path
     * @throws ConnectionException
     */
    @Connect
    @TestConnectivity
    public void connect(@ConnectionKey @FriendlyName("Domain") String domain, @ConnectionKey @FriendlyName("Host") String host,
            @ConnectionKey @Optional @FriendlyName("Path") String path, @FriendlyName("Username") String username, @Password @FriendlyName("Password") String password,
            @Optional @FriendlyName("Connection timeout") String timeout) throws ConnectionException {

        try {
            this.setDomain(domain);
            this.setUsername(username);
            this.setPassword(password);
            this.setHost(host);
            this.setPath(Utilities.normalizePath(path));
            if (NumberUtils.isNumber(timeout)) {
                this.connectionTimeout = Integer.parseInt(timeout);
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
     */
    @ValidateConnection
    public boolean isConnected() {
        return this.getSmbClient().isConnected();
    }

    /**
     * get connection Id
     */
    @ConnectionIdentifier
    public String connectionId() {
        return this.getSmbClient().connectionId();
    }

}