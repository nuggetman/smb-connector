/**
 * Copyright 2018-2020 (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.smb;

import java.util.List;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.mule.api.ConnectionException;
import org.mule.api.annotations.Config;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.display.FriendlyName;
import org.mule.api.annotations.licensing.RequiresEnterpriseLicense;
import org.mule.api.annotations.param.ConnectionKey;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;
import org.mule.api.annotations.param.RefOnly;
import org.mule.modules.smb.config.SmbConnectorConfig;
import org.mule.modules.smb.exception.SmbConnectionException;
import org.mule.modules.smb.utils.Utilities;

@Connector(name = "smb", friendlyName = "SMB Connector", minMuleVersion = "3.8.0")
@RequiresEnterpriseLicense(allowEval = true)
public class SmbConnector {

    @Config
    SmbConnectorConfig config;

    /**
     * Read file processor for reading in the contents of a file
     *
     * @param fileName,
     *            name of file to read in
     * @param dirName,
     *            directory where file is located
     * @param autoDelete,
     *            Should the file be deleted after reading
     * @return The file contents as a byte[]
     * @throws SmbConnectionException
     *             when a connection error occurs
     */
    @Processor
    public byte[] fileRead(@ConnectionKey @FriendlyName("File Name") String fileName,
            @FriendlyName("Directory Name") @Default("") String dirName,
            @FriendlyName("Delete after reading") @Default("false") boolean autoDelete) throws ConnectionException {
        return this.getConfig().getSmbClient().readFile(fileName, dirName, autoDelete);
    }

    /**
     * Write file processor for writing out data to a file
     *
     * @param fileName,
     *            file name to be used for the write operation.
     * @param dirName,
     *            directory where file is located
     * @param append,
     *            when true append payload to file, if it exists already
     * @param fileContent,
     *            a byte[], String or InputStream containing the contents of the
     *            file to write.
     * @param encoding,
     *            character encoding of contents to write
     * @return boolean, true indicates success for the operation
     * @throws SmbConnectionException
     *             when a connection error occurs
     */
    @Processor
    public boolean fileWrite(@ConnectionKey @FriendlyName("File Name") @Required String fileName,
            @FriendlyName("Directory Name") @Default("") String dirName,
            @FriendlyName("Append to file") @Default("false") boolean append,
            @RefOnly @Default("#[payload]") Object fileContent, @Required @Default("UTF-8") String encoding)
            throws ConnectionException {
        return this.getConfig().getSmbClient().writeFile(fileName, dirName, append, fileContent, encoding);
    }

    /**
     * Delete file processor for deleting a file
     *
     * @param fileName,
     *            file name to be used for the delete operation.
     * @param dirName,
     *            directory where file is located
     * @return boolean, true indicates success for the operation
     * @throws SmbConnectionException
     *             when a connection error occurs
     */
    @Processor
    public boolean fileDelete(@ConnectionKey @FriendlyName("File Name") String fileName,
            @FriendlyName("Directory Name") @Default("") String dirName) throws ConnectionException {
        return this.getConfig().getSmbClient().deleteFile(fileName, dirName);
    }

    /**
     * List directory processor for retrieving the contents of a directory
     *
     * @param dirName,
     *            directory name to be used for the list operation.
     * @param wildcard,
     *            DOS style wildcard filter
     * @return A list of Maps, each Map containing attributes for each file
     * @throws SmbConnectionException
     *             when a connection error occurs
     */
    @Processor
    public List<String> directoryList(@ConnectionKey @FriendlyName("Folder Name") @Optional String dirName,
            @FriendlyName("Wildcard") @Default("*.*") String wildcard) throws ConnectionException {
        String w = wildcard;
        if (!Utilities.isNotBlankOrEmptyOrNull(w)) {
            w = "*.*";
        }
        return this.getConfig().getSmbClient().listDirectory(dirName, w);
    }

    /**
     * Create directory processor for creating a directory
     *
     * @param dirName,
     *            directory name to be used for the write operation.
     * @return boolean, true indicates success for the operation
     * @throws SmbConnectionException
     *             when a connection error occurs
     */
    @Processor
    public boolean directoryCreate(@ConnectionKey @Required @FriendlyName("Folder Name") String dirName)
            throws ConnectionException {
        return this.getConfig().getSmbClient().createDirectory(dirName);
    }

    /**
     * Delete directory processor for deleting a directory
     *
     * @param dirName,
     *            directory name to be used for the delete operation.
     * @param recursive,
     *            set to true for a recursive delete
     * @return boolean, true indicates success for the operation
     * @throws SmbConnectionException
     *             when a connection error occurs
     */
    @Processor
    public boolean directoryDelete(@ConnectionKey @FriendlyName("Directory Name") String dirName,
            @FriendlyName("Recursive delete") @Default("false") boolean recursive) throws ConnectionException {
        return this.getConfig().getSmbClient().deleteDir(dirName, recursive);
    }

    /**
     * Set the config
     *
     * @param config,
     *            SMBConnectorConfig to be used
     */
    public void setConfig(SmbConnectorConfig config) {
        this.config = config;
    }

    /**
     * Convenience method for getting the config
     *
     * @return The config as an SMBConnectorConfig
     */
    public SmbConnectorConfig getConfig() {
        return config;
    }
}