/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.modules.smb.internal;

import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.commons.io.IOUtils;
import org.mule.api.ConnectionExceptionCode;
import org.mule.modules.smb.config.SMBConnectorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

public class SmbClient {

    private static final Logger logger = LoggerFactory.getLogger(SmbClient.class);

    private SMBConnectorConfig config;

    private SmbFile smbFile;

    public SmbClient(SMBConnectorConfig config) {
        this.config = config;
    }

    public void setSmbFile(SmbFile smbFile) {
        this.smbFile = smbFile;
    }

    private SmbFile getSmbFile() {
        return this.smbFile;
    }

    public boolean connect(String path, String host, NtlmPasswordAuthentication credentials) throws Exception {
        logger.debug("connecting to: smb://" + host + path);
        smbFile = new SmbFile("smb://" + host + path, this.getConfig().getCredentials());

        if (!smbFile.canRead()) {
            throw new org.mule.api.ConnectionException(ConnectionExceptionCode.UNKNOWN, null, "not connected", null);
        }

        this.setSmbFile(smbFile);
        return isConnected();
    }

    public byte[] readFile(String fileName) {

        byte[] data = null;
        SmbFileInputStream smbFileInputStream = null;

        try {
            SmbFile localSmbFile = new SmbFile("smb://" + this.getSmbFile().getServer() + "/" + this.getSmbFile().getShare() + "/" + fileName, this.getConfig().getCredentials());

            logger.debug("Reading in file: " + localSmbFile.getUncPath());
            if (localSmbFile.canRead()) {
                smbFileInputStream = new SmbFileInputStream(localSmbFile);
                data = new byte[(int) localSmbFile.length()];
                smbFileInputStream.read(data);
                logger.debug("Done reading file:" + localSmbFile.getUncPath());
            } else {
                logger.debug("unable to read file: " + localSmbFile.getUncPath());
            }
        } catch (Exception e) {
            logger.error("unable to read file: " + fileName, e.getMessage());
        }

        return data;
    }

    /**
     * Disconnect
     */
    public void disconnect() {
        // currently no way to close connections via jcifs
    }

    /**
     * Are we connected
     */
    public boolean isConnected() {
        try {
            if (smbFile.canRead()) {
                return true;
            } else {
                return false;
            }
        } catch (SmbException e) {
            logger.error("Error checking connection status: " + e.getLocalizedMessage());
            return false;
        }
    }

    /**
     * Return a default connection id
     */
    public String connectionId() {
        return "001";
    }

    public void createDirectory(String dirName) throws MalformedURLException, SmbException {
        try {
            SmbFile directory = new SmbFile("smb://" + this.getSmbFile().getServer() + "/" + this.getSmbFile().getShare() + "/" + dirName, this.getConfig().getCredentials());

            logger.debug("Creating a dir: " + directory.getUncPath());

            if (!directory.exists()) {
                directory.mkdirs();
                logger.debug("Done creating dir:" + directory.getUncPath());
            } else {
                logger.debug("unable to create dir: " + directory.getUncPath());
            }
        } catch (Exception e) {
            logger.error("unable to create dir: " + dirName, e.getMessage());
        }
    }

    public boolean checkIsFileOldEnough(SmbFile file, long minimumAge) {
        if (minimumAge > 0) {
            long lastMod = file.getLastModified();
            long now = System.currentTimeMillis();
            long currentAge = now - lastMod;

            if (logger.isDebugEnabled()) {
                logger.debug("fileAge = " + currentAge + ", expected = " + minimumAge + ", now = " + now + ", lastMod = " + lastMod);
            }

            if (currentAge < minimumAge) {
                if (logger.isInfoEnabled()) {
                    logger.info("The file has not aged enough yet, will return nothing for: " + file.getName());
                }
                return false;
            }
        }
        return true;
    }

    public SMBConnectorConfig getConfig() {
        return this.config;
    }

    public void writeFile(String fileName, Object data) throws Exception {
        // OutputStream out = connector.getOutputStream(getEndpoint(), event);

        SmbFile smbFile = new SmbFile("smb://" + this.getSmbFile().getServer() + "/" + this.getSmbFile().getShare() + "/" + fileName, this.getConfig().getCredentials());

        SmbFileOutputStream out = new SmbFileOutputStream(smbFile);

        try {
            if (data instanceof InputStream) {
                InputStream is = ((InputStream) data);
                IOUtils.copy(is, out);
                is.close();
            } else if (data instanceof byte[]) {
                byte[] dataBytes;
                dataBytes = (byte[]) data;
                IOUtils.write(dataBytes, out);
            } else {
                throw new Exception("unsupported object type");
            }
        } finally {
            out.flush();
            out.close();
        }

    }

}
