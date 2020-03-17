
package org.mule.extension.smb;

import org.mule.extension.smb.config.SmbConnectorConfig;
import org.mule.extension.smb.connection.SmbConnectorConfigConnectionProvider;
import org.mule.extension.smb.operation.SmbConnectorOperations;
import org.mule.extension.smb.utils.Utilities;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;

import java.util.List;

@Extension(name = "SMB Connector")
@ConnectionProviders({ SmbConnectorConfigConnectionProvider.class })
@Operations(SmbConnectorOperations.class)
public class SmbExtension {


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
     * @param encoding,
     *            character encoding of contents to write
     * @return The file contents as a byte[]
     * @throws SmbConnectionException
     *             when a connection error occurs
     */
    public byte[] fileRead(String fileName, String dirName, boolean autoDelete)
            throws ConnectionException {
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
     *            a byte[], String or InputStream containing the contents of the file to write.
     * @param encoding,
     *            character encoding of contents to write
     * @return boolean, true indicates success for the operation
     * @throws SmbConnectionException
     *             when a connection error occurs
     */
    public boolean fileWrite(String fileName, String dirName, boolean append, Object fileContent,
                             String encoding) throws ConnectionException {
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
    public boolean fileDelete(String fileName, String dirName) throws ConnectionException {
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
    public List<String> directoryList(String dirName, String wildcard) throws ConnectionException {
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
    public boolean directoryCreate(String dirName) throws ConnectionException {
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
    public boolean directoryDelete(String dirName, boolean recursive) throws ConnectionException {
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
