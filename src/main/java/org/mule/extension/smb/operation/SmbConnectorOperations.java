
package org.mule.extension.smb.operation;

import org.mule.extension.smb.config.SmbConnectorConfig;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

import java.util.List;

public class SmbConnectorOperations {

    /**
     * Read file processor for reading in the contents of a file
     * 
     */
    public byte[] fileRead(@Connection SmbConnectorConfig connection, @DisplayName("File Name") String fileName,
                           @Optional(defaultValue = "") @DisplayName("Directory Name") String dirName,
                           @Optional(defaultValue = "false") @DisplayName("Delete after reading") boolean autoDelete)
            throws ConnectionException {
        SmbConnector connector = new SmbConnector();
        connector.setConfig(connection);
        byte[] result = connector.fileRead(fileName, dirName, autoDelete);
        return result;
    }

    /**
     * Write file processor for writing out data to a file
     * 
     */
    public boolean fileWrite(@Connection SmbConnectorConfig connection, @DisplayName("File Name") String fileName,
            @Optional(defaultValue = "") @DisplayName("Directory Name") String dirName,
            @Optional(defaultValue = "false") @DisplayName("Append to file") boolean append,
            @Content Object fileContent, @Optional(defaultValue = "UTF-8") String encoding) throws ConnectionException {
        SmbConnector connector = new SmbConnector();
        connector.setConfig(connection);
        boolean result = connector.fileWrite(fileName, dirName, append, fileContent, encoding);
        return result;
    }

    /**
     * Delete file processor for deleting a file
     * 
     */
    public boolean fileDelete(@Connection SmbConnectorConfig connection, @DisplayName("File Name") String fileName,
            @Optional(defaultValue = "") @DisplayName("Directory Name") String dirName) throws ConnectionException {
        SmbConnector connector = new SmbConnector();
        connector.setConfig(connection);
        boolean result = connector.fileDelete(fileName, dirName);
        return result;
    }

    /**
     * List directory processor for retrieving the contents of a directory
     * 
     */
    public List<String> directoryList(@Connection SmbConnectorConfig connection,
            @Optional @DisplayName("Folder Name") String dirName,
            @Optional(defaultValue = "*.*") @DisplayName("Wildcard") String wildcard) throws ConnectionException {
        SmbConnector connector = new SmbConnector();
        connector.setConfig(connection);
        List<String> result = connector.directoryList(dirName, wildcard);
        return result;
    }

    /**
     * Create directory processor for creating a directory
     * 
     */
    public boolean directoryCreate(@Connection SmbConnectorConfig connection,
            @DisplayName("Folder Name") String dirName) throws ConnectionException {
        SmbConnector connector = new SmbConnector();
        connector.setConfig(connection);
        boolean result = connector.directoryCreate(dirName);
        return result;
    }

    /**
     * Delete directory processor for deleting a directory
     * 
     */
    public boolean directoryDelete(@Connection SmbConnectorConfig connection,
            @DisplayName("Directory Name") String dirName,
            @Optional(defaultValue = "false") @DisplayName("Recursive delete") boolean recursive)
            throws ConnectionException {
        SmbConnector connector = new SmbConnector();
        connector.setConfig(connection);
        boolean result = connector.directoryDelete(dirName, recursive);
        return result;
    }

}
