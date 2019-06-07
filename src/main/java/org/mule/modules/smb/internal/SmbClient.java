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
import org.mule.api.ConnectionExceptionCode;
import org.mule.modules.smb.config.SmbConnectorConfig;
import org.mule.modules.smb.exception.SmbConnectionException;
import org.mule.modules.smb.exception.SmbConnectorException;
import org.mule.modules.smb.utils.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jcifs.smb.DosFileFilter;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;


public class SmbClient {

    private static final Logger logger = LoggerFactory.getLogger(SmbClient.class);

    private SmbConnectorConfig config;

    private NtlmPasswordAuthentication credentials;

    public SmbClient(SmbConnectorConfig config) {
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
            if (this.getConnection() != null) {
                return this.getConnection().canRead();
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("Error checking connection status", e);
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
    private boolean checkIsFileOldEnough(SmbFile file) {
    		if (this.getConfig().getFileage() > 0) {
            long lastMod = file.getLastModified();
            long now = System.currentTimeMillis();
            long currentAge = now - lastMod;
            if (currentAge < 0) {
                logger.warn("The system clocks appear to be out of sync, either time or timezone");
            }
            if (currentAge < this.getConfig().getFileage()) {
                logger.debug("The file has not aged enough yet, will return nothing for: " + file.getName());
                return false;
            }
        }
        return true;
    }

    /**
     * 
     * @return SmbConnectorConfig containing configuration parameters for this client
     */
    public SmbConnectorConfig getConfig() {
        return this.config;
    }

    /**
     * 
     * @return boolean stating whether client connected correctly
     * @throws Exception
     */
    public boolean connect() throws SmbConnectionException {
        try {
            logger.debug("connecting to: smb://" + this.getConfig().getHost() + this.getConfig().getPath());
            
            this.setCredentials(this.getConfig().getDomain(), this.getConfig().getUsername(), this.getConfig().getPassword());
            SmbFile smbFile = this.getConnection();
            
            if (!smbFile.canRead()) {
                throw new SmbConnectionException(ConnectionExceptionCode.CANNOT_REACH, null, "not connected", null);
            }
        } catch (SmbAuthException e) {
            throw new SmbConnectionException(ConnectionExceptionCode.INCORRECT_CREDENTIALS, null, e.getMessage(), e);
        } catch (SmbException e) {
            throw new SmbConnectionException(ConnectionExceptionCode.CANNOT_REACH, null, e.getMessage(), e);
        }
        return isConnected();
    }

    /**
     * 
     * Read in some file content from the specified file name
     * 
     * @param fileName
     *            String containing the filename of the file to read in
     * @param autoDelete
     *            boolean to indicate whether file should be deleted after reading
     * @return byte[] of file content
     */
    public byte[] readFile(String fileName, String dirName, boolean autoDelete) throws SmbConnectionException {
        byte[] data = null;
        SmbFileInputStream smbFileInputStream = null;
        try {
            SmbFile smbFile = this.getConnection(Utilities.normalizeDir(dirName) + Utilities.normalizeFile(fileName));
            if (smbFile != null) {
                if (checkIsFileOldEnough(smbFile)) {
                    smbFileInputStream = new SmbFileInputStream(smbFile);
                    data = new byte[(int) smbFile.length()];
                    smbFileInputStream.read(data);
                    smbFileInputStream.close();
                    if (autoDelete) {
                    		smbFile.delete();
                    }
                }
                logger.debug("Done reading file", smbFile.getUncPath());

            } else {
                logger.error("file not found", fileName);
            }
        } catch (SmbAuthException e) {
            throw new SmbConnectionException(ConnectionExceptionCode.INCORRECT_CREDENTIALS, "READ_ERROR", e.getMessage(), e);
        } catch (SmbException e) {
            throw new SmbConnectionException(ConnectionExceptionCode.CANNOT_REACH, "READ_ERROR", e.getMessage(), e);
        } catch (MalformedURLException e) {
            throw new SmbConnectionException(ConnectionExceptionCode.UNKNOWN, "READ_ERROR", e.getMessage(), e);
        } catch (UnknownHostException e) {
            throw new SmbConnectionException(ConnectionExceptionCode.UNKNOWN_HOST, "READ_ERROR", e.getMessage(), e);
        } catch (IOException e) {
            throw new SmbConnectionException(ConnectionExceptionCode.UNKNOWN, "READ_ERROR", e.getMessage(), e);
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
     * @param encoding
     * 			  Character encoding of contents to write
     */
    public void writeFile(String fileName, String dirName, boolean append, Object data, String encoding) throws SmbConnectionException {
        SmbFileOutputStream out = null;
        try {
            SmbFile smbFile = this.getConnection(Utilities.normalizeDir(dirName) + Utilities.normalizeFile(fileName));
            out = new SmbFileOutputStream(smbFile, append);
            if (data instanceof InputStream) {
                InputStream is = (InputStream) data;
                IOUtils.copy(is, out);
                is.close();
            } else if (data instanceof byte[]) {
                byte[] dataBytes = (byte[]) data;
                IOUtils.write(dataBytes, out);
            } else if (data instanceof String) {
                byte[] dataBytes = ((String) data).getBytes(encoding);
                IOUtils.write(dataBytes, out);
            } else {
                logger.error("unsupported object type for file write: " + data.getClass() + ", supported types are InputStream, String or byte[]");
            }
        } catch (SmbAuthException e) {
            throw new SmbConnectionException(ConnectionExceptionCode.INCORRECT_CREDENTIALS, "WRITE_ERROR", e.getMessage(), e);
        } catch (SmbException e) {
            throw new SmbConnectionException(ConnectionExceptionCode.CANNOT_REACH, "WRITE_ERROR", e.getMessage(), e);
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
    }

    /**
     * Delete the specified file
     * 
     * @param fileName
     *            String value of the file name to delete
     */
    public boolean deleteFile(String fileName, String dirName) throws SmbConnectionException {
        SmbFile smbFile = this.getConnection(Utilities.normalizeDir(dirName) + Utilities.normalizeFile(fileName));
        if (smbFile != null) {
            try {
                if (smbFile.isFile()) {
                		if (checkIsFileOldEnough(smbFile)) {
                			smbFile.delete();
                			logger.debug("deleted file: " + fileName);
                			return true;
                		} else {
                			logger.debug("file:" + fileName + " not old enough for deletion");
                			return false;
                		}
                } else {
                    logger.debug("not a file: " + fileName);
                    return false;
                }
            } catch (SmbAuthException e) {
                throw new SmbConnectionException(ConnectionExceptionCode.INCORRECT_CREDENTIALS, null, e.getMessage(), e);
            } catch (SmbException e) {
                throw new SmbConnectionException(ConnectionExceptionCode.CANNOT_REACH, null, e.getMessage(), e);
            }
        } else {
        		return false;
        }
    }
    
    /**
     * Create a new directory
     * 
     * @param dirName
     *            String value of the directory name
     */
    public void createDirectory(String dirName) throws SmbConnectionException {
        SmbFile directory = this.getConnection(Utilities.normalizeFile(dirName));

        logger.debug("creating a directory: " + directory.getUncPath());

        try {
            if (!directory.exists()) {
                directory.mkdirs();
                logger.debug("done creating directory:" + directory.getUncPath());
            } else {
                logger.debug("directory already exists: " + directory.getUncPath());
            }
        } catch (SmbAuthException e) {
            throw new SmbConnectionException(ConnectionExceptionCode.INCORRECT_CREDENTIALS, null, e.getMessage(), e);
        } catch (SmbException e) {
            throw new SmbConnectionException(ConnectionExceptionCode.CANNOT_REACH, null, e.getMessage(), e);
        }
    }

    /**
     * Delete the specified directory
     * 
     * @param dirName
     *            String value of the directory to delete
     */
    public boolean deleteDir(String dirName) throws SmbConnectionException {
        SmbFile smbFile = this.getConnection(Utilities.normalizeDir(dirName));
        try {
            if (smbFile.isDirectory()) {
            		smbFile.delete();
            		return true;
            } else {
                logger.debug("not a directory: " + dirName);
                return false;
            }
        } catch (SmbAuthException e) {
            throw new SmbConnectionException(ConnectionExceptionCode.INCORRECT_CREDENTIALS, null, e.getMessage(), e);
        } catch (SmbException e) {
            throw new SmbConnectionException(ConnectionExceptionCode.CANNOT_REACH, null, e.getMessage(), e);
        }
    }

    /**
     * Retrieve the directory listing
     * 
     * @param dirName
     *            String of the directory name
     * @param wildcard
     *            String of the DOS wildcard filter
     * @return A List<Map<String,Object>> where each item in the list is a file or directory and the Map structure contains the attributes for the item
     * @throws SmbConnectionException, SmbConnectorException 
     * 
     */
    public List<Map<String, Object>> listDirectory(String dirName, String wildcard) throws SmbConnectionException, SmbConnectorException {
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        SmbFile smbDir;
        try {
            if (dirName != null) {
                smbDir = this.getConnection(Utilities.normalizeDir(dirName));
            } else {
                smbDir = this.getConnection();
            }
            if (smbDir != null) {
                SmbFile[] smbFiles;
				DosFileFilter filter = new DosFileFilter(wildcard, 32);
				try {
					smbFiles = smbDir.listFiles(filter);
	                for (SmbFile file : smbFiles) {
	                		if (checkIsFileOldEnough(file)) {
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
				} catch (SmbException e) {
					if (e.getNtStatus() == SmbException.NT_STATUS_NO_SUCH_FILE) {
						// do nothing - no files found
					}
					else {
						throw e;
					}
				}
            }
        } catch (SmbAuthException e) {
            throw new SmbConnectionException(ConnectionExceptionCode.INCORRECT_CREDENTIALS, null, e.getMessage(), e);
        } catch (SmbException e) {
            throw new SmbConnectorException(e.getMessage(), e);
        }
        return results;
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
    		if (username != null && password != null && domain != null) {
    			if (username.length() != 0 && password.length() != 0) {
	    			NtlmPasswordAuthentication c = new NtlmPasswordAuthentication(domain, username, password);
	    	        this.credentials = c;
    			} else {
    				this.credentials = NtlmPasswordAuthentication.ANONYMOUS;
	    	        logger.warn("Warning - anonymous connectivity used");
    			}
    		}
    		else {
    			this.credentials = NtlmPasswordAuthentication.ANONYMOUS;
    			logger.warn("Warning - anonymous connectivity used");
    		}
    }
    
    /**
     * Helper method to create a connection
     * 
     * @return SmbFile
     */
    private SmbFile getConnection() throws SmbConnectionException {
    		return this.getConnection("");
    }
    
    /**
     * Helper method to create a connection for a file or directory
     * 
     * @param String filename
     * 		filename or directory to obtain
     * @return SmbFile
     */
    private SmbFile getConnection(String filenameordirectory) throws SmbConnectionException {
    		try {    		
			SmbFile f;
	    		if (this.getConfig().getHost() != null && this.getConfig().getPath() != null) {
			    	if (this.getCredentials() != null) {
			    		f = new SmbFile(this.getConfig().getHost() + this.getConfig().getPath() + filenameordirectory, this.getCredentials());
			    	} else 
			    		f = new SmbFile(this.getConfig().getHost() + this.getConfig().getPath() + filenameordirectory);
			    	
			    f.setConnectTimeout(this.getConfig().getTimeout());
		    
			    return f;
	    		} else {
	    			return null;
	    		}
		} catch (MalformedURLException e) {
            throw new SmbConnectionException(ConnectionExceptionCode.UNKNOWN, null, e.getMessage(), e);
		}
    }
}
    
    
