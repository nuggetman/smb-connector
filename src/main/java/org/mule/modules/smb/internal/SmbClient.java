/**
 * Copyright 2018-2020 (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.smb.internal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.mule.api.ConnectionExceptionCode;
import org.mule.modules.smb.config.SmbConnectorConfig;
import org.mule.modules.smb.exception.SmbConnectionException;
import org.mule.modules.smb.utils.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.msfscc.FileAttributes;
import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2CreateOptions;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.SmbConfig;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.share.File;

public class SmbClient {

    private static final Logger logger = LoggerFactory.getLogger(SmbClient.class);

    private SmbConnectorConfig connectorConfig;

    private AuthenticationContext ac = null;

    private SmbConfig smbConfig = null;

    private SMBClient sc = null;

    private Session smbSession = null;

    private DiskShare diskShare = null;

    public SmbClient(SmbConnectorConfig config) {
        this.connectorConfig = config;
    }

    /**
     * Disconnect client
     */
    public void disconnect() {
        this.closeShare();
        this.closeSession();
        this.closeConnection();
        this.isConnected();
    }

    /**
     * Disconnect share
     */
    private void closeShare() {
        try {
            this.getShare().close();
        } catch (IOException e) {
            logger.info("Disconnection error", e);
        }
    }

    /**
     * Disconnect session
     */
    private void closeSession() {
        try {
            this.getSession().close();
        } catch (Exception e) {
            // do nothing - SMBJ has an async close process
            logger.debug("Ignorable (usually) error closing out session ", e.getLocalizedMessage(), e);
        }
    }

    /**
     * Disconnect connection
     */
    private void closeConnection() {
        try {
            this.getSession().getConnection().close();
        } catch (Exception e) {
            // do nothing - SMBJ has an async close process
            logger.debug("Ignorable (usually) error closing out session ", e.getLocalizedMessage(), e);
        }
    }

    /**
     * Check to see if we are connected
     * 
     * @return boolean, true if connected
     */
    public boolean isConnected() {
        boolean c = false;
        try {
            if (shareSessionNull() && shareSessionConnected()) {
                c = this.getShare().isConnected();
            }
        } catch (Exception e) {
            logger.error("Error checking connection status", e);
        }
        logger.debug("Connected status ", c);
        return c;
    }

    /**
     * Check to see if share & session are valid
     * 
     * @return boolean, true if valid
     */
    private boolean shareSessionNull() {
        return (this.getShare() != null && this.getSession() != null);
    }

    /**
     * Check to see if share & session are connected
     * 
     * @return boolean, true if connected
     */
    private boolean shareSessionConnected() {
        return (this.getShare().isConnected() && this.getSession().getConnection().isConnected());
    }

    /**
     * Return a default connection id
     * 
     * @return String, connection id
     */
    public String connectionId() {
        return "001";
    }

    /**
     * 
     * @param age
     *            epoch long value of file in ms
     * 
     * @param minimumAge
     *            long value in ms of minimum file age
     * @return
     */
    private boolean checkIsFileOldEnough(long age) {
        return Utilities.timeCompare(this.getConfig().getFileage(), age);
    }

    /**
     * 
     * @return SmbConnectorConfig containing configuration parameters for this
     *         client
     */
    public SmbConnectorConfig getConfig() {
        return this.connectorConfig;
    }

    /**
     * 
     * @return boolean, states whether client connected correctly
     * @throws SmbConnectionException
     *             when unable to connect to share
     */
    public boolean connect() throws SmbConnectionException {
        try {
            logger.debug("connecting to: smb://", this.getConfig().getHost(), "/", this.getConfig().getShare());

            logger.debug("setting auth context");
            this.setAuthContext();

            logger.debug("setting smbConfig");
            this.smbConfig = SmbConfig.builder().withTimeout(this.getConfig().getTimeout(), TimeUnit.MILLISECONDS)
                    .withSoTimeout(30, TimeUnit.SECONDS).build();

            logger.debug("setting smbClient");
            sc = new SMBClient(this.smbConfig);

            logger.debug("setting smbSession");
            setSession(this.getConfig().getHost());

            logger.debug("setting smbShare");
            setShare(this.getConfig().getHost(), this.getConfig().getShare());

        } catch (Exception e) {
            throw new SmbConnectionException(ConnectionExceptionCode.CANNOT_REACH, null, e.getMessage(), e);
        }
        return isConnected();
    }

    /**
     * 
     * Read in some file content from the specified file name
     * 
     * @param fileName,
     *            filename of the file to read in
     * @param dirName,
     *            directory where file is located
     * @param autoDelete,
     *            indicate whether file should be deleted after reading
     * @return byte[] of file content
     * @throws SmbConnectionException
     *             when a connection error occurs
     */
    public byte[] readFile(String fileName, String dirName, boolean autoDelete) throws SmbConnectionException {
        InputStream is = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            Set<SMB2ShareAccess> s = new HashSet<>();
            s.add(SMB2ShareAccess.FILE_SHARE_DELETE);
            s.add(SMB2ShareAccess.FILE_SHARE_READ);

            File smbFile = this.getShare().openFile(Utilities.buildPath(dirName, fileName),
                    EnumSet.of(AccessMask.GENERIC_READ), null, s, SMB2CreateDisposition.FILE_OPEN, null);
            if (checkIsFileOldEnough(
                    smbFile.getFileInformation().getBasicInformation().getChangeTime().toEpochMillis())) {
                is = smbFile.getInputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                is.close();
                smbFile.close();
                if (autoDelete) {
                    deleteFile(fileName, dirName);
                }
            }
            logger.debug("Done reading file ", smbFile.getFileName());

        } catch (MalformedURLException e) {
            throw new SmbConnectionException(ConnectionExceptionCode.UNKNOWN, "READ_ERROR", e.getMessage(), e);
        } catch (UnknownHostException e) {
            throw new SmbConnectionException(ConnectionExceptionCode.UNKNOWN_HOST, "READ_ERROR", e.getMessage(), e);
        } catch (IOException e) {
            throw new SmbConnectionException(ConnectionExceptionCode.UNKNOWN, "READ_ERROR", e.getMessage(), e);
        }
        this.disconnect();
        return os.toByteArray();
    }

    /**
     * Write some content to the specified file name
     * 
     * @param fileName,
     *            name of the file to write
     * @param dirName,
     *            directory where file should be written
     * @param append,
     *            boolean indicating whether to append this new content to the file
     *            otherwise overwrite
     * @param data,
     *            InputStream, byte[] or String of data to write into the file
     * @param encoding,
     *            Character encoding of contents to write
     * @throws SmbConnectionException
     *             when a connection error occurs
     */
    public void writeFile(String fileName, String dirName, boolean append, Object data, String encoding)
            throws SmbConnectionException {
        OutputStream out = null;
        try {
            Set<FileAttributes> fileAttributes = new HashSet<>();
            fileAttributes.add(FileAttributes.FILE_ATTRIBUTE_NORMAL);
            Set<SMB2CreateOptions> createOptions = new HashSet<>();
            createOptions.add(SMB2CreateOptions.FILE_RANDOM_ACCESS);
            createOptions.add(SMB2CreateOptions.FILE_NON_DIRECTORY_FILE);

            File f;
            if (append) {
                f = this.getShare().openFile(Utilities.buildPath(dirName, fileName), EnumSet.of(AccessMask.GENERIC_ALL),
                        fileAttributes, SMB2ShareAccess.ALL, SMB2CreateDisposition.FILE_OPEN_IF, createOptions);
            } else {
                f = this.getShare().openFile(Utilities.buildPath(dirName, fileName), EnumSet.of(AccessMask.GENERIC_ALL),
                        fileAttributes, SMB2ShareAccess.ALL, SMB2CreateDisposition.FILE_OVERWRITE_IF, createOptions);
            }

            out = f.getOutputStream(append);

            if (data instanceof InputStream) {
                InputStream in = (InputStream) data;
                byte[] buffer = new byte[1024];
                while (in.read(buffer) > -1) {
                    out.write(buffer);
                }
                out.flush();
                out.close();
                in.close();
            } else if (data instanceof byte[]) {
                byte[] dataBytes = (byte[]) data;
                out.write(dataBytes);
                out.flush();
                out.close();
            } else if (data instanceof String) {
                byte[] dataBytes = ((String) data).getBytes(encoding);
                out.write(dataBytes);
                out.flush();
                out.close();
            } else {
                logger.error("unsupported object type for file write ", data.getClass(),
                        ", supported types are InputStream, String or byte[]");
            }
            f.close();
            out = null;
        } catch (com.hierynomus.mssmb2.SMBApiException e) {
            throw new SmbConnectionException(ConnectionExceptionCode.UNKNOWN, "WRITE_ERROR", e.getMessage(), e);
        } catch (MalformedURLException e) {
            throw new SmbConnectionException(ConnectionExceptionCode.UNKNOWN, "WRITE_ERROR", e.getMessage(), e);
        } catch (UnknownHostException e) {
            throw new SmbConnectionException(ConnectionExceptionCode.UNKNOWN_HOST, "WRITE_ERROR", e.getMessage(), e);
        } catch (IOException e) {
            throw new SmbConnectionException(ConnectionExceptionCode.UNKNOWN, "WRITE_ERROR", e.getMessage(), e);
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    logger.error("error closing out file writer", e);
                }
            }
        }
        this.disconnect();
    }

    /**
     * Delete the specified file
     * 
     * @param fileName,
     *            name of the file to delete
     * @param dirName,
     *            directory where file is located
     * @return boolean, successful if true
     * @throws SmbConnectionException
     *             when a connection error occurs
     */
    public boolean deleteFile(String fileName, String dirName) throws SmbConnectionException {
        boolean status = false;
        try {
            String absoluteFile = Utilities.buildPath(dirName, fileName);
            if (this.getShare().fileExists(absoluteFile)) {
                if (checkIsFileOldEnough(this.getShare().getFileInformation(absoluteFile).getBasicInformation()
                        .getChangeTime().toEpochMillis())) {
                    this.getShare().rm(absoluteFile);
                    logger.debug("deleted file ", absoluteFile);
                    status = true;
                } else {
                    logger.debug("file ", absoluteFile, " not old enough for deletion");
                }
            } else {
                logger.debug("file does not exist ", absoluteFile);
            }
        } catch (Exception e) {
            throw new SmbConnectionException(ConnectionExceptionCode.UNKNOWN, null, e.getMessage(), e);
        }
        this.disconnect();
        return status;
    }

    /**
     * Create a new directory
     * 
     * @param dirName,
     *            name of the directory
     * @return boolean, successful if true
     * @throws SmbConnectionException
     *             when a connection error occurs
     */
    public boolean createDirectory(String dirName) throws SmbConnectionException {
        boolean status = false;
        if (dirName != null) {
            try {
                if (!this.getShare().folderExists(Utilities.cleanPath(dirName))) {
                    this.getShare().mkdir(Utilities.cleanPath(dirName));
                    logger.debug("done creating directory ", Utilities.cleanPath(dirName));
                    status = true;
                } else {
                    logger.debug("directory already exists ", Utilities.cleanPath(dirName));
                }
            } catch (Exception e) {
                throw new SmbConnectionException(ConnectionExceptionCode.UNKNOWN, null, e.getMessage(), e);
            }
        }
        this.disconnect();
        return status;
    }

    /**
     * Delete the specified directory
     * 
     * @param dirName,
     *            name of the directory to delete
     * @param recursive,
     *            set to true for a recursive delete
     * @return boolean, successful if true
     * @throws SmbConnectionException
     *             when a connection error occurs
     */
    public boolean deleteDir(String dirName, boolean recursive) throws SmbConnectionException {
        boolean status = false;
        if (dirName != null) {
            try {
                if (this.getShare().folderExists(Utilities.cleanPath(dirName))) {
                    this.getShare().rmdir(Utilities.cleanPath(dirName), recursive);
                    logger.debug("done deleting directory ", Utilities.cleanPath(dirName));
                    status = true;
                } else {
                    logger.debug("directory already exists ", Utilities.cleanPath(dirName));
                }
            } catch (Exception e) {
                throw new SmbConnectionException(ConnectionExceptionCode.UNKNOWN, null, e.getMessage(), e);
            }
        }
        this.disconnect();
        return status;
    }

    /**
     * Retrieve the directory listing
     * 
     * @param dirName,
     *            directory name
     * @param wildcard,
     *            DOS wildcard filter
     * @return A List where each item in the list is a file or directory name
     * @throws SmbConnectionException
     *             when a connection error occurs
     */
    public List<String> listDirectory(String dirName, String wildcard) throws SmbConnectionException {
        List<String> results = new ArrayList<String>();

        try {
            for (FileIdBothDirectoryInformation f : this.getShare().list(Utilities.cleanPath(dirName), wildcard)) {
                if (checkIsFileOldEnough(f.getChangeTime().toEpochMillis()) && !f.getFileName().equalsIgnoreCase(".")
                        && !f.getFileName().equalsIgnoreCase("..")) {
                    results.add(f.getFileName());
                }
            }
        } catch (Exception e) {
            throw new SmbConnectionException(ConnectionExceptionCode.UNKNOWN, null, e.getMessage(), e);
        }
        this.disconnect();
        return results;
    }

    /**
     * Set the credentials to re-use for connections
     */
    private void setAuthContext() {
        if (!connectorConfig.getGuest() && !connectorConfig.getAnonymous()) {
            ac = new AuthenticationContext(this.getConfig().getUsername(), this.getConfig().getPassword().toCharArray(),
                    this.getConfig().getDomain());
        } else if (connectorConfig.getGuest()) {
            this.ac = AuthenticationContext.guest();
            logger.debug("guest credentials used");
        } else if (connectorConfig.getAnonymous()) {
            this.ac = AuthenticationContext.anonymous();
            logger.debug("anonymous credentials used");
        }
    }

    /**
     * get current diskshare
     * 
     * @return DiskShare
     */
    private DiskShare getShare() {
        return this.diskShare;
    }

    /**
     * Helper method to create a connection for a server share
     * 
     * @param String
     *            hostname, hostname to connect to
     * @param String
     *            sharename, sharename to connect to
     * @return void
     */
    private void setShare(String hostname, String sharename) {
        this.diskShare = (DiskShare) this.getSession().connectShare(sharename);
    }

    /**
     * get current session
     * 
     * @return Session authenticated object
     */
    private Session getSession() {
        return this.smbSession;
    }

    /**
     * Helper method to create a connection session for a server
     * 
     * @param String
     *            hostname, hostname to connect to
     * @return void
     */
    private void setSession(String hostname) throws IOException {
        Connection c = this.sc.connect(hostname);
        this.smbSession = c.authenticate(ac);
    }
}
