/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.modules.smb;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.mule.api.annotations.Config;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.display.FriendlyName;
import org.mule.api.annotations.param.ConnectionKey;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;
import org.mule.api.annotations.param.RefOnly;
import org.mule.modules.smb.config.SMBConnectorConfig;
import org.mule.modules.smb.utils.Utilities;

@Connector(name = "SMB", friendlyName = "SMB Connector")
public class SmbConnector {

    // private static final Logger logger = LoggerFactory.getLogger(SmbConnector.class);

    @Config
    SMBConnectorConfig config;

    /**
     * Read file processor for reading in the contents of a file
     *
     * @param fileName
     *            File name to read in
     * @param fileAge
     *            Required file age before read can occur (ms)
     * @param autoDelete
     *            Should the file be deleted after reading
     * @return The file contents as a byte[]
     */
    @Processor
    public byte[] fileRead(@ConnectionKey @FriendlyName("File Name") String fileName, @Default("500") @FriendlyName("File age (ms)") Integer fileAge,
            @Default("false") @FriendlyName("Delete after reading") boolean autoDelete) {
        return this.getConfig().getSmbClient().readFile(fileName, fileAge, autoDelete);
    }

    /**
     * Write file processor for writing out data to a file
     *
     * @param fileName
     *            File name to be used for the write operation.
     * @param append
     *            Append payload to file, if it exists already
     * @param fileContent
     *            A byte[], String or InputStream containing the contents of the file to write.
     * @return void
     */
    @Processor
    public boolean fileWrite(@ConnectionKey @FriendlyName("File Name") @Required String fileName, @Default("false") @FriendlyName("Append to file") boolean append,
            @RefOnly @Default("#[payload]") Object fileContent) {
        return this.getConfig().getSmbClient().writeFile(fileName, append, fileContent);
    }

    /**
     * Delete file processor for deleting a file
     *
     * @param fileName
     *            File name to be used for the delete operation.
     * @return void
     */
    @Processor
    public boolean fileDelete(@ConnectionKey @FriendlyName("File Name") String fileName) {
        return this.getConfig().getSmbClient().deleteFile(fileName);
    }

    /**
     * List directory processor for retrieving the contents of a directory
     *
     * @param dirName
     *            Folder name to be used for the list operation.
     * @param wildcard
     *            DOS style wildcard filter
     * @return A list of Maps, each Map containing attributes for each file
     */
    @Processor
    public List<Map<String, Object>> directoryList(@ConnectionKey @FriendlyName("Folder Name") @Optional String dirName, @Default("*") @FriendlyName("Wildcard") String wildcard) {
        if (!Utilities.isNotBlankOrEmptyOrNull(wildcard)) {
            wildcard = "*";
        }
        return this.getConfig().getSmbClient().listDirectory(dirName, wildcard);
    }

    /**
     * Create directory processor for creating a directory
     *
     * @param dirName
     *            Folder name to be used for the write operation.
     * @return void
     */
    @Processor
    public boolean directoryCreate(@ConnectionKey @Required @FriendlyName("Folder Name") String dirName) {
        return this.getConfig().getSmbClient().createDirectory(dirName);
    }

    /**
     * Delete direcotry processor for deleting a directory
     *
     * @param dirName
     *            Directory name to be used for the delete operation.
     * @return void
     */
    @Processor
    public boolean directoryDelete(@ConnectionKey @FriendlyName("Directory Name") String dirName) {
        return this.getConfig().getSmbClient().deleteDir(dirName);
    }

    /**
     * Set the config
     *
     * @param config
     *            SMBConnectorConfig to be used
     * @return void
     */
    public void setConfig(SMBConnectorConfig config) {
        this.config = config;
    }

    /**
     * Convenience method for getting the config
     *
     * @return The config as an SMBConnectorConfig
     */
    public SMBConnectorConfig getConfig() {
        return config;
    }
}