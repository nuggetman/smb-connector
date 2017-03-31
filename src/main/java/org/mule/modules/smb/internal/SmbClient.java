/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.modules.smb.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.modules.smb.config.SMBConnectorConfig;
import org.mule.modules.smb.utils.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

public class SmbClient {

    private static final Logger LOG = LoggerFactory.getLogger(SmbClient.class);

    private SMBConnectorConfig config;

    private NtlmPasswordAuthentication credentials;

    private SmbFile smbRootDir;

    public SmbClient(SMBConnectorConfig config) {
        this.config = config;
    }

    /**
     * Disconnect client
     */
    public void disconnect() {
        // currently no way to close connections via jcifs - connections automatically disconnect after the timeout (15000ms default)
    }

    /**
     * Check to see if we are connected
     */
    public boolean isConnected() {
        try {
            if (getRootSmbDir().canRead()) {
                return true;
            } else {
                return false;
            }
        } catch (NullPointerException npe) {
            return false;
        } catch (SmbException e) {
            LOG.error("Error checking connection status", e.getLocalizedMessage());
            return false;
        }
    }

    /**
     * Return a default connection id
     */
    public String connectionId() {
        return "001";
    }

    /**
     * 
     * @param file
     *            SmbFile to check age of
     * 
     * @param minimumAge
     *            long value in ms of minimum file age
     * @return
     */
    public boolean checkIsFileOldEnough(SmbFile file, long minimumAge) {
        if (minimumAge > 0) {
            long lastMod = file.getLastModified();
            long now = System.currentTimeMillis();
            long currentAge = now - lastMod;
            if (LOG.isDebugEnabled()) {
                LOG.debug("fileAge = " + currentAge + ", expected = " + minimumAge + ", now = " + now + ", lastMod = " + lastMod);
            }
            if (currentAge < minimumAge) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("The file has not aged enough yet, will return nothing for: " + file.getName());
                }
                return false;
            }
        }
        return true;
    }

    /**
     * 
     * @return SmbConnectorConfig containing configuration parameters for this client
     */
    public SMBConnectorConfig getConfig() {
        return this.config;
    }

    /**
     * 
     * @return boolean stating whether client connected correctly
     * @throws Exception
     */
    public boolean connect() throws MalformedURLException, SmbAuthException, SmbException, ConnectionException {
        LOG.debug("connecting to: smb://" + this.getConfig().getHost() + this.getConfig().getPath());
        this.setCredentials(this.getConfig().getDomain(), this.getConfig().getUsername(), this.getConfig().getPassword());
        SmbFile smbFile = new SmbFile("smb://" + this.getConfig().getHost() + this.getConfig().getPath(), this.getCredentials());
        smbFile.setConnectTimeout(this.getConfig().getTimeout());
        if (!smbFile.canRead()) {
            throw new ConnectionException(ConnectionExceptionCode.UNKNOWN, null, "not connected", null);
        }
        return isConnected();
    }

    /**
     * 
     * Read in some file content from the specified file name
     * 
     * @param fileName
     *            String containing the filename of the file to read in
     * @param fileAge
     *            Integer of the minimum file age before file is read in
     * 
     * @param autoDelete
     *            boolean to indicate whether file should be deleted after reading
     * @return byte[] of file content
     */
    public byte[] readFile(String fileName, Integer fileAge, boolean autoDelete) {
        byte[] data = null;
        SmbFileInputStream smbFileInputStream = null;
        try {
            SmbFile smbFile = getSmbFileFromRoot(fileName);
            if (smbFile.exists()) {
                if (smbFile.canRead()) {
                    if (checkIsFileOldEnough(smbFile, fileAge)) {
                        smbFileInputStream = new SmbFileInputStream(smbFile);
                        data = new byte[(int) smbFile.length()];
                        smbFileInputStream.read(data);
                        if (autoDelete) {
                            this.deleteFile(smbFile);
                        }
                        smbFileInputStream.close();
                    }
                    LOG.debug("Done reading file", smbFile.getUncPath());
                } else {
                    LOG.info("unable to read file", smbFile.getUncPath());
                }
            } else {
                LOG.error("file not found", fileName);
            }
        } catch (NullPointerException e) {
            LOG.error("smbfile not defined", e.getLocalizedMessage());
        } catch (SmbAuthException e) {
            LOG.error("smb authentication error", e.getLocalizedMessage());
        } catch (SmbException e) {
            LOG.error("smb connection error", e.getLocalizedMessage());
        } catch (MalformedURLException e) {
            LOG.error("malformed url error", e.getLocalizedMessage());
        } catch (UnknownHostException e) {
            LOG.error("unknown host error", e.getLocalizedMessage());
        } catch (IOException e) {
            LOG.error("I/O error", e.getLocalizedMessage());
        }
        return data;
    }

    /**
     * Write some content to the specified file name
     * 
     * @param fileName
     *            String containing the filename of the file to read in
     * @param append
     *            boolean indicating whether to append this new content to the file otherwise overwrite
     * @param data
     *            InputStream or byte[] of data to write into the file
     */
    public boolean writeFile(String fileName, boolean append, Object data) {
        boolean success = false;
        try {
            SmbFile smbFile = getSmbFileFromRoot(fileName);
            SmbFileOutputStream out = null;
            try {
                out = new SmbFileOutputStream(smbFile, append);
                if (data instanceof InputStream) {
                    InputStream is = ((InputStream) data);
                    IOUtils.copy(is, out);
                    is.close();
                    success = true;
                } else if (data instanceof byte[]) {
                    byte[] dataBytes = (byte[]) data;
                    IOUtils.write(dataBytes, out);
                    success = true;
                } else if (data instanceof String) {
                    byte[] dataBytes = ((String) data).getBytes();
                    IOUtils.write(dataBytes, out);
                    success = true;
                } else {
                    LOG.error("unsupported object type", data.getClass().toString());
                }
            } finally {
                out.flush();
                out.close();
            }
        } catch (SmbAuthException sae) {
            LOG.error("insufficient permissions to write file", sae.getLocalizedMessage());
        } catch (SmbException se) {
            LOG.error("error writing out file", se.getLocalizedMessage());
        } catch (MalformedURLException mue) {
            LOG.error("malformed file path for", fileName + ", " + mue.getLocalizedMessage());
        } catch (UnknownHostException uhe) {
            LOG.error("unknown host error writing to: " + fileName, uhe.getLocalizedMessage());
        } catch (IOException ie) {
            LOG.error("i/o error", ie.getLocalizedMessage());
        }
        return success;
    }

    /**
     * Create a new directory
     * 
     * @param dirName
     *            String value of the directory name
     */
    public boolean createDirectory(String dirName) {
        boolean success = false;
        try {
            SmbFile directory = getSmbFileFromRoot(dirName);

            LOG.debug("creating a directory: " + directory.getUncPath());

            if (!directory.exists()) {
                directory.mkdirs();
                success = true;
                LOG.debug("done creating directory:" + directory.getUncPath());
            } else {
                LOG.debug("directory already exists: " + directory.getUncPath());
            }
        } catch (SmbAuthException sae) {
            LOG.error("insufficient permissions to create: " + dirName, sae.getMessage());
        } catch (SmbException se) {
            LOG.error("unable to create directory: " + dirName, se.getMessage());
        }
        return success;
    }

    /**
     * Delete the specified file
     * 
     * @param fileName
     *            String value of the file name to delete
     */
    public boolean deleteFile(String fileName) {
        boolean success = false;
        try {
            SmbFile smbFile = getSmbFileFromRoot(fileName);
            if (smbFile.isFile()) {
                success = this.deleteFile(smbFile);
            }
        } catch (SmbException e) {
            LOG.error("unable to determine type for deletion: " + fileName);
        }
        return success;
    }

    /**
     * Delete the specified directory
     * 
     * @param dirName
     *            String value of the directory to delete
     */
    public boolean deleteDir(String dirName) {
        boolean success = false;
        try {
            SmbFile smbFile = getSmbDirFromRoot(dirName);
            if (smbFile.isDirectory()) {
                success = this.deleteFile(smbFile);
            }
        } catch (SmbException e) {
            LOG.error("unable to determine type for deletion: " + dirName);
        }
        return success;
    }

    /**
     * Delete an SmbFile
     * 
     * @param smbFile
     *            SmbFile to delete
     */
    private boolean deleteFile(SmbFile smbFile) {
        boolean success = false;
        try {
            smbFile.delete();
            success = true;
        } catch (SmbAuthException sae) {
            LOG.error("insufficient permissions to delete file: " + smbFile.getUncPath() + ", " + sae.getLocalizedMessage());
        } catch (SmbException se) {
            LOG.error("unable to delete file: " + smbFile.getName() + ", " + se.getLocalizedMessage());
        }
        return success;
    }

    /**
     * Retrieve the directory listing
     * 
     * @param dirName
     *            String of the directory name
     * @param wildcard
     *            String of the DOS wildcard filter
     * @return A List<Map<String,Object>> where each item in the list is a file or directory and the Map structure contains the attributes for the item
     */
    public List<Map<String, Object>> listDirectory(String dirName, String wildcard) {
        List<Map<String, Object>> results = null;
        try {
            SmbFile smbDir;
            if (dirName != null) {
                smbDir = getSmbDirFromRoot(dirName);
            } else {
                smbDir = getRootSmbDir();
            }
            if (smbDir != null) {
                SmbFile[] smbFiles;
                if (smbDir.canRead() && smbDir.isDirectory()) {
                    smbFiles = smbDir.listFiles(wildcard);
                    if (smbFiles.length > 0) {
                        results = new ArrayList<Map<String, Object>>();
                        for (SmbFile file : smbFiles) {
                            HashMap<String, Object> metaData = new HashMap<String, Object>();
                            metaData.put("name", file.getName());
                            metaData.put("last modified", file.getLastModified());
                            metaData.put("created on", file.createTime());
                            metaData.put("size", file.length());
                            metaData.put("is file", file.isFile());
                            metaData.put("is directory", file.isDirectory());
                            metaData.put("read-only", !file.canWrite());
                            metaData.put("hidden", file.isHidden());
                            results.add(metaData);
                        }
                    }
                } else {
                    LOG.error("cannot read: " + smbDir.canRead());
                }
            }
        } catch (SmbAuthException sae) {
            LOG.error("insufficient permissions to list directory: " + dirName, sae.getMessage());
        } catch (SmbException se) {
            LOG.error("unable to list directory: " + dirName, se.getMessage());
        }

        return results;
    }

    /**
     * Helper method for ensuring path naming consistency for files
     * 
     * @param fileName
     *            String value of the file name to append to the root folder
     * @return SmbFile object based on appended file name
     */
    private SmbFile getSmbFileFromRoot(String fileName) {
        try {
            SmbFile f = new SmbFile(this.getRootSmbDir().getPath() + Utilities.normalizeFile(fileName), this.getCredentials());
            f.setConnectTimeout(this.getConfig().getTimeout());
            return f;
        } catch (MalformedURLException mue) {
            LOG.error("malformed file path: " + this.getRootSmbDir().getPath() + Utilities.normalizeFile(fileName) + ", " + mue.getLocalizedMessage());
            return null;
        }
    }

    /**
     * Helper method for ensuring path naming consistency for directories
     * 
     * @param folderName
     *            String value of the folder name to append to the root folder
     * @return SmbFile object based on appended folder name
     */
    private SmbFile getSmbDirFromRoot(String folderName) {
        try {
            SmbFile f = new SmbFile(this.getRootSmbDir().getPath() + Utilities.normalizePath(folderName), this.getCredentials());
            f.setConnectTimeout(this.getConfig().getTimeout());
            return f;
        } catch (MalformedURLException mue) {
            LOG.error("malformed file path: " + this.getRootSmbDir().getPath() + Utilities.normalizePath(folderName) + ", " + mue.getLocalizedMessage());
            return null;
        }
    }

    /**
     * Helper method to track base path for SMB mount
     * 
     * @return SmbFile for the base mount path
     */
    private SmbFile getRootSmbDir() {
        try {
            if (this.smbRootDir != null) {
                return this.smbRootDir;
            } else {
                if (this.getConfig().getHost() != null && this.getConfig().getPath() != null) {

                    SmbFile f = new SmbFile("smb://" + this.getConfig().getHost() + this.getConfig().getPath(), this.getCredentials());
                    f.setConnectTimeout(this.getConfig().getTimeout());
                    return f;
                } else {
                    return null;
                }
            }
        } catch (MalformedURLException mue) {
            LOG.error("malformed file path: " + this.getConfig().getHost() + this.getConfig().getPath() + ", " + mue.getLocalizedMessage());
            return null;
        }
    }

    /**
     * Get NtlmPasswordAuthentication object required for connecting to SmbFile objects
     * 
     * @return NtlmPasswordAuthentication object
     */
    public NtlmPasswordAuthentication getCredentials() {
        return credentials;
    }

    /**
     * Set the credentials to re-use for connections
     *
     * @return void
     */
    public void setCredentials(String domain, String username, String password) {
        NtlmPasswordAuthentication credentials = new NtlmPasswordAuthentication(domain, username, password);
        this.credentials = credentials;
    }

}
