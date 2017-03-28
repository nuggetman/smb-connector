package org.mule.modules.smb.config;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.MuleContext;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Connect;
import org.mule.api.annotations.ConnectionIdentifier;
import org.mule.api.annotations.Disconnect;
import org.mule.api.annotations.TestConnectivity;
import org.mule.api.annotations.ValidateConnection;
import org.mule.api.annotations.components.ConnectionManagement;
import org.mule.api.annotations.display.FriendlyName;
import org.mule.api.annotations.display.Password;
import org.mule.api.annotations.param.ConnectionKey;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;
import org.mule.modules.smb.internal.SmbClient;
import org.mule.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jcifs.smb.NtlmPasswordAuthentication;

@ConnectionManagement(friendlyName = "Configuration")
public class SMBConnectorConfig {

    /**
     * File Age
     */
    @Configurable
    @Default("500")
    @FriendlyName("File age (ms)")
    private long fileAge;

    private static final String DEFAULT_WORK_FILENAME_PATTERN = "#[function:uuid].#[function:systime].#[header:inbound:originalFilename]";

    private final SmbClient smbClient = new SmbClient(this);

    private static final Logger logger = LoggerFactory.getLogger(SMBConnectorConfig.class);

    // message properties
    public static final String PROPERTY_FILENAME = "filename";
    public static final String PROPERTY_ORIGINAL_FILENAME = "originalFilename";
    public static final String PROPERTY_FILE_SIZE = "fileSize";

    private NtlmPasswordAuthentication credentials;

    private String moveToPattern = null;

    private String writeToDirectoryName = null;

    private String moveToDirectoryName = null;

    private String workDirectoryName = null;

    private String workFileNamePattern = DEFAULT_WORK_FILENAME_PATTERN;

    private String readFromDirectoryName = null;

    private String outputPattern = null;

    private boolean outputAppend = false;

    private boolean autoDelete = true;

    private boolean checkFileAge = false;

    private boolean streaming = true;

    private boolean recursive = false;

    private String fileName;

    /**
     * Mule context.
     */
    @Inject
    private MuleContext muleContext;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName(String fileName) {
        return this.fileName;
    }

    public String getMoveToDirectory() {
        return moveToDirectoryName;
    }

    public void setMoveToDirectory(String dir) {
        this.moveToDirectoryName = dir;
    }

    public void setWorkDirectory(String workDirectoryName) throws IOException {
        this.workDirectoryName = workDirectoryName;
        // if (workDirectoryName != null) {
        /// File workDirectory = FileUtils.openDirectory(workDirectoryName);
        // if (!workDirectory.canWrite()) {
        // throw new IOException(
        /// "Error on initialization, Work Directory '" + workDirectory + "' is
        // not writeable");
        // }
        // }
    }

    public String getWorkDirectory() {
        return workDirectoryName;
    }

    public NtlmPasswordAuthentication getCredentials() {
        return credentials;
    }

    public void setCredentials(String domain, String username, String password) {
        NtlmPasswordAuthentication credentials = new NtlmPasswordAuthentication(domain, username, password);
        this.credentials = credentials;
    }

    public void setCredentials(NtlmPasswordAuthentication credentials) {
        this.credentials = credentials;
    }

    public void setWorkFileNamePattern(String workFileNamePattern) {
        this.workFileNamePattern = workFileNamePattern;
    }

    public String getWorkFileNamePattern() {
        return workFileNamePattern;
    }

    public boolean isOutputAppend() {
        return outputAppend;
    }

    public void setOutputAppend(boolean outputAppend) {
        this.outputAppend = outputAppend;
    }

    public String getOutputPattern() {
        return outputPattern;
    }

    public void setOutputPattern(String outputPattern) {
        this.outputPattern = outputPattern;
    }

    public long getFileAge() {
        return fileAge;
    }

    public boolean getCheckFileAge() {
        return checkFileAge;
    }

    public void setFileAge(long fileAge) {
        this.fileAge = fileAge;
        this.checkFileAge = true;
    }

    public String getWriteToDirectory() {
        return writeToDirectoryName;
    }

    public void setWriteToDirectory(String dir) throws IOException {
        this.writeToDirectoryName = dir;
        if (writeToDirectoryName != null) {
            File writeToDirectory = FileUtils.openDirectory(writeToDirectoryName);
            if (!writeToDirectory.canWrite()) {
                throw new IOException("Error on initialization, " + writeToDirectory + " does not exist or is not writeable");
            }
        }
    }

    public String getReadFromDirectory() {
        return readFromDirectoryName;
    }

    public void setReadFromDirectory(String dir) throws IOException {
        this.readFromDirectoryName = dir;
        if (readFromDirectoryName != null) {
            // check if the directory exists/can be read
            FileUtils.openDirectory((readFromDirectoryName));
        }
    }

    public boolean isAutoDelete() {
        return autoDelete;
    }

    public void setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
    }

    public String getMoveToPattern() {
        return moveToPattern;
    }

    public void setMoveToPattern(String moveToPattern) {
        this.moveToPattern = moveToPattern;
    }

    public boolean isStreaming() {
        return streaming;
    }

    public void setStreaming(boolean streaming) {
        this.streaming = streaming;
    }

    public boolean isRecursive() {
        return recursive;
    }

    public void setRecursive(boolean recursive) {
        this.recursive = recursive;
    }

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
    public void connect(@ConnectionKey @FriendlyName("Domain") String domain, @ConnectionKey @FriendlyName("Host") String host, @ConnectionKey @FriendlyName("Path") String path,
            @Optional @FriendlyName("Username") String username, @Password @Optional @FriendlyName("Password") String password) throws ConnectionException {

        this.setCredentials(domain, username, password);

        try {
            this.getSmbClient().connect(path, host, this.getCredentials());

        } catch (Exception e) {
            throw new org.mule.api.ConnectionException(ConnectionExceptionCode.UNKNOWN, null, e.getMessage(), e);
        }

    }

    /**
     * Disconnect
     */
    @Disconnect
    public void disconnect() {
        // this.getSmbClient().disconnect();
    }

    /**
     * Are we connected
     */
    @ValidateConnection
    public boolean isConnected() {
        // return this.getSmbClient().isConnected();
        return false; // for now return false to kick things off
    }

    /**
     * Are we connected
     */
    @ConnectionIdentifier
    public String connectionId() {
        return this.getSmbClient().connectionId();
    }

    /**
     * Getter method for mule context.
     *
     * @return mule context
     */
    public MuleContext getMuleContext() {
        return muleContext;
    }

    /**
     * Setter method for mule context.
     *
     * @param muleContext
     *            - mule context
     */
    public void setMuleContext(MuleContext muleContext) {
        this.muleContext = muleContext;
    }

}

// public static final String FILE = "file";
// These are properties that can be overridden on the Receiver by the
// endpoint declaration
// inbound only
// public static final String PROPERTY_FILE_AGE = "fileAge";
// public static final String PROPERTY_MOVE_TO_PATTERN = "moveToPattern";
// public static final String PROPERTY_MOVE_TO_DIRECTORY =
// "moveToDirectory";
// public static final String PROPERTY_READ_FROM_DIRECTORY =
// "readFromDirectoryName";

// outbound only
// public static final String PROPERTY_OUTPUT_PATTERN = "outputPattern";
// public static final String PROPERTY_ORIGINAL_DIRECTORY =
// "originalDirectory";
// public static final String PROPERTY_DIRECTORY = "directory";
// public static final String PROPERTY_SOURCE_FILENAME = "sourceFileName";
// public static final String PROPERTY_SOURCE_DIRECTORY = "sourceDirectory";
// public static final String PROPERTY_WRITE_TO_DIRECTORY =
// "writeToDirectoryName";

// public static final String PROPERTY_FILE_TIMESTAMP = "timestamp";
