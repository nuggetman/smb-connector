
package org.mule.extension.smb.connection;

import org.mule.extension.smb.config.SmbConnectorConfig;
import org.mule.extension.smb.utils.Utilities;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.connection.PoolingConnectionProvider;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

@Alias("SmbConnectorConfig")
public class SmbConnectorConfigConnectionProvider implements PoolingConnectionProvider<SmbConnectorConfig> {

    @Parameter
    @Optional
    @DisplayName("Domain")
    public String domain;

    @Parameter
    @DisplayName("Host")
    public String host;

    @Parameter
    @Optional
    @DisplayName("Share")
    public String share;

    @Parameter
    @Optional
    @DisplayName("Username")
    public String username;

    @Parameter
    @Optional
    @DisplayName("Password")
    public String password;

    @Parameter
    @Optional
    @DisplayName("Connection timeout")
    public String timeout;

    @Parameter
    @Optional
    @DisplayName("File age (ms)")
    public String fileage;

    @Override
    public SmbConnectorConfig connect() throws ConnectionException {
        SmbConnectorConfig connectionClient = new SmbConnectorConfig();
        try {
            connectionClient.connect(getDomain(), getHost(), getShare(), getUsername(), getPassword(), getTimeout(),
                    getFileage());
        } catch (Exception oldConnectionException) {
            throw new ConnectionException(oldConnectionException.getMessage());
        }
        return connectionClient;
    }

    /**
     * Sets domain
     * 
     * @param value
     *            Value to set
     */
    public void setDomain(String value) {
        this.domain = value;
    }

    /**
     * Retrieves domain
     * 
     */
    public String getDomain() {
        return this.domain;
    }

    /**
     * Sets host
     * 
     * @param value
     *            Value to set
     */
    public void setHost(String value) {
        this.host = value;
    }

    /**
     * Retrieves host
     * 
     */
    public String getHost() {
        return this.host;
    }

    /**
     * Sets share
     * 
     * @param value
     *            Value to set
     */
    public void setShare(String value) {
        this.share = value;
    }

    /**
     * Retrieves share
     * 
     */
    public String getShare() {
        return this.share;
    }

    /**
     * Sets username
     * 
     * @param value
     *            Value to set
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Retrieves username
     * 
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Sets password
     * 
     * @param value
     *            Value to set
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Retrieves password
     * 
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Sets timeout
     * 
     * @param value
     *            Value to set
     */
    public void setTimeout(String value) {
        this.timeout = value;
    }

    /**
     * Retrieves timeout
     * 
     */
    public String getTimeout() {
        return this.timeout;
    }

    /**
     * Sets fileage
     * 
     * @param value
     *            Value to set
     */
    public void setFileage(String value) {
        this.fileage = value;
    }

    /**
     * Retrieves fileage
     * 
     */
    public String getFileage() {
        return this.fileage;
    }

    @Override
    public void disconnect(SmbConnectorConfig connection) {
        connection.disconnect();
    }

    @Override
    public ConnectionValidationResult validate(SmbConnectorConfig connection) {
        try {
            if (connection.isConnected()) {
                return ConnectionValidationResult.success();
            } else {
                return ConnectionValidationResult.failure("Connection is no longer valid",
                        new Exception("Connection is no longer valid"));
            }
        } catch (Exception e) {
            return ConnectionValidationResult.failure("Connection is no longer valid", e);
        }
    }
}
