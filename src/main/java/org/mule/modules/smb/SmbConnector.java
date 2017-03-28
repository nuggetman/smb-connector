/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.modules.smb;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

import java.net.MalformedURLException;
import java.util.Map;

import javax.inject.Inject;

import org.mule.api.MuleContext;
import org.mule.api.annotations.Config;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.display.FriendlyName;
import org.mule.api.annotations.lifecycle.Start;
import org.mule.api.annotations.param.ConnectionKey;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.RefOnly;
import org.mule.modules.smb.config.SMBConnectorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jcifs.smb.SmbException;

@Connector(name = "SMB", friendlyName = "SMB Connector")
// @OnException(handler=ErrorHandler.class)
public class SmbConnector {

    private static final Logger logger = LoggerFactory.getLogger(SmbConnector.class);

    @Config
    SMBConnectorConfig config;

    // The connector asks for the manager of the mule context
    @Inject
    protected MuleContext muleContext;

    /**
     * Takes all the steps needed in order to initialize this class.
     */
    @Start
    public void initialize() {
        this.getConfig().setMuleContext(muleContext);
    }

    /**
     * Read file processor for reading in the contents of a file
     *
     * @param fileName
     *            File name to read in
     * @return The file contents as a byte[]
     */
    @Processor
    public byte[] readFile(@ConnectionKey @FriendlyName("File Name") String fileName) {
        return this.getConfig().getSmbClient().readFile(fileName);
    }

    /**
     * Write file processor for writing out data to a file
     *
     * @param fileName
     *            Folder qualififed file name to be used for the write operation.
     * @param fileContent
     *            A byte[] or inputstream containing the contents of the file to write.
     * @return void
     */
    @Processor
    public void write(@ConnectionKey @FriendlyName("File Name") String fileName, @RefOnly @Default("#[payload]") Object fileContent) {
        // Return a boolean?
        checkArgument(!isNullOrEmpty(fileName));
        try {
            this.getConfig().getSmbClient().writeFile(fileName, fileContent);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getLocalizedMessage());
        }
    }

    /**
     * Delete file processor for deleting a file
     *
     * @param fileName
     *            File name to be used for the delete operation.
     * @return void
     */
    @Processor
    public void delete(@ConnectionKey @FriendlyName("File Name") String fileName) {
        // Return a boolean?
    }

    /**
     * List directory processor for retrieving the contents of a directory
     *
     * @param dirName
     *            Folder name to be used for the list operation.
     * @return void
     */
    @Processor
    public Map<?, ?> listDir(@ConnectionKey @FriendlyName("Folder Name") String dirName) {
        // Return a map of files & metadata?
        return null;
    }

    /**
     * Create directory processor for creating a directory
     *
     * @param dirName
     *            Folder name to be used for the write operation.
     * @return void
     */
    @Processor
    public void create(@ConnectionKey @FriendlyName("Folder Name") String dirName) {
        try {
            this.getConfig().getSmbClient().createDirectory(dirName);
        } catch (MalformedURLException | SmbException e) {
            // TODO Auto-generated catch block
            logger.error("Error creating directory: ", e.getLocalizedMessage());
        }
    }

    public SMBConnectorConfig getConfig() {
        return config;
    }

    public void setConfig(SMBConnectorConfig config) {
        this.config = config;
    }

    public MuleContext getMuleContext() {
        return muleContext;
    }

    public void setMuleContext(MuleContext muleContext) {
        this.muleContext = muleContext;
    }

}