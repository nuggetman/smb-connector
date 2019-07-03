/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
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
import com.hierynomus.msdtyp.FileTime;
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
        // currently no way to close connections via jcifs - connections automatically disconnect after the timeout
    }

    /**
     * Check to see if we are connected
     */
    public boolean isConnected() {
        boolean c = false;
        try {
            if (this.getShare() != null) {
                c = this.getShare().isConnected();
        		}
        } catch (Exception e) {
            logger.error("Error checking connection status", e);
        }
        return c;
    }

    /**
     * Return a default connection id
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
        if (this.getConfig().getFileage() > 0) {
            long currentAge = FileTime.now().toEpochMillis() - age;
            if (currentAge < 0) {
                logger.warn("The system clocks appear to be out of sync, either time or timezone");
            }
            if (currentAge < this.getConfig().getFileage()) {
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
        return this.connectorConfig;
    }

    /**
     * 
     * @return boolean stating whether client connected correctly
     * @throws Exception
     */
    public boolean connect() throws SmbConnectionException {
        try {
            logger.debug("connecting to: smb://" + this.getConfig().getHost() + this.getConfig().getShare());
            
            if (this.ac == null) {
                this.setAuthContext();
                logger.info("setting auth context");
            }
    			
            if (this.smbConfig == null) {
                logger.info("setting smbConfig");
                this.smbConfig = SmbConfig.builder()
                .withTimeout(this.getConfig().getTimeout(), TimeUnit.MILLISECONDS)
                .build();
            }
            
            if (this.sc == null) {
                logger.info("setting smbClient");
                sc = new SMBClient(this.smbConfig);
            }

            if (getSession() == null) {
                logger.info("setting smbSession");
                setSession(this.getConfig().getHost());
            }
            
            if (getShare() == null) {
                logger.info("setting smbShare");
                setShare(this.getConfig().getHost(), this.getConfig().getShare());
	        }

        } catch (IOException e) {
            throw new SmbConnectionException(ConnectionExceptionCode.CANNOT_REACH, null, e.getMessage(), e);
        } catch (Exception e) {
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
        InputStream is = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            Set<SMB2ShareAccess> s = new HashSet<>();
            s.add(SMB2ShareAccess.FILE_SHARE_DELETE);
            s.add(SMB2ShareAccess.FILE_SHARE_READ);

            File smbFile = this.getShare().openFile(Utilities.buildPath(dirName, fileName), EnumSet.of(AccessMask.GENERIC_READ), null, s, SMB2CreateDisposition.FILE_OPEN, null);
            if (checkIsFileOldEnough(smbFile.getFileInformation().getBasicInformation().getChangeTime().toEpochMillis())) {
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
            logger.debug("Done reading file", smbFile.getFileName());

        } catch (MalformedURLException e) {
            throw new SmbConnectionException(ConnectionExceptionCode.UNKNOWN, "READ_ERROR", e.getMessage(), e);
        } catch (UnknownHostException e) {
            throw new SmbConnectionException(ConnectionExceptionCode.UNKNOWN_HOST, "READ_ERROR", e.getMessage(), e);
        } catch (IOException e) {
            throw new SmbConnectionException(ConnectionExceptionCode.UNKNOWN, "READ_ERROR", e.getMessage(), e);
        }
        return os.toByteArray();
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
        OutputStream out = null;
        try {
            Set<FileAttributes> fileAttributes = new HashSet<>();
            fileAttributes.add(FileAttributes.FILE_ATTRIBUTE_NORMAL);
            Set<SMB2CreateOptions> createOptions = new HashSet<>();
            createOptions.add(SMB2CreateOptions.FILE_RANDOM_ACCESS);
            createOptions.add(SMB2CreateOptions.FILE_NON_DIRECTORY_FILE);
            
            File f;
            if (append) {
                f = this.getShare().openFile(Utilities.buildPath(dirName, fileName), EnumSet.of(AccessMask.GENERIC_ALL), fileAttributes, SMB2ShareAccess.ALL, SMB2CreateDisposition.FILE_OPEN, createOptions);
            } else {
                f = this.getShare().openFile(Utilities.buildPath(dirName, fileName), EnumSet.of(AccessMask.GENERIC_ALL), fileAttributes, SMB2ShareAccess.ALL, SMB2CreateDisposition.FILE_CREATE, createOptions);
            }

            out = f.getOutputStream();
            
            if (data instanceof InputStream) {
                InputStream in = (InputStream) data;
                byte[] buffer = new byte[1024];
                while(in.read(buffer) > -1) {
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
                logger.error("unsupported object type for file write: " + data.getClass() + ", supported types are InputStream, String or byte[]");
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
    }

    /**
     * Delete the specified file
     * 
     * @param fileName
     *            String value of the file name to delete
     */
    public boolean deleteFile(String fileName, String dirName) throws SmbConnectionException {
        try {
            String absoluteFile = Utilities.buildPath(dirName, fileName);
            if (this.getShare().fileExists(absoluteFile)) {
                if (checkIsFileOldEnough(this.getShare().getFileInformation(absoluteFile).getBasicInformation().getChangeTime().toEpochMillis())) {
                    this.getShare().rm(absoluteFile);
                    logger.debug("deleted file: " + absoluteFile);
                    return true;
                } else {
                    logger.debug("file:" + absoluteFile + " not old enough for deletion");
                    return false;
                }
            } else {
                logger.debug("file does not exist: " + absoluteFile);
                return false;
            }
        } catch (Exception e) {
            throw new SmbConnectionException(ConnectionExceptionCode.UNKNOWN, null, e.getMessage(), e);
        }
    }
    
    /**
     * Create a new directory
     * 
     * @param dirName
     *            String value of the directory name
     */
    public void createDirectory(String dirName) throws SmbConnectionException {
        if (dirName != null) {
            try {
                if (!this.getShare().folderExists(Utilities.cleanPath(dirName))) {
                    this.getShare().mkdir(Utilities.cleanPath(dirName));
                    logger.debug("done creating directory:" + Utilities.cleanPath(dirName));
    				} else {
                    logger.debug("directory already exists: " + Utilities.cleanPath(dirName));
    	            }
    			} catch (Exception e) {
                throw new SmbConnectionException(ConnectionExceptionCode.UNKNOWN, null, e.getMessage(), e);
            }
    		}
    }

    /**
     * Delete the specified directory
     * 
     * @param dirName
     *            String value of the directory to delete
     */
    public boolean deleteDir(String dirName, boolean recursive) throws SmbConnectionException {
        if (dirName != null) {
			try {
				if (this.getShare().folderExists(Utilities.cleanPath(dirName))) {
					this.getShare().rmdir(Utilities.cleanPath(dirName), recursive);
					logger.debug("done deleting directory:" + Utilities.cleanPath(dirName));
					return true;
				} else {
	                logger.debug("directory already exists: " + Utilities.cleanPath(dirName));
	                return false;
	            }
			} catch (Exception e) {
		        throw new SmbConnectionException(ConnectionExceptionCode.UNKNOWN, null, e.getMessage(), e);
	        }
		} else {
			return false;
		}
    }

    /**
     * Retrieve the directory listing
     * 
     * @param dirName
     *            String of the directory name
     * @param wildcard
     *            String of the DOS wildcard filter
     * @return A List<String> where each item in the list is a file or directory name
     * @throws SmbConnectionException, SmbConnectorException 
     * 
     */
    public List<String> listDirectory(String dirName, String wildcard) throws SmbConnectionException {
        List<String> results = new ArrayList<String>();
        
        try {
            for (FileIdBothDirectoryInformation f : this.getShare().list(Utilities.cleanPath(dirName), wildcard)) {
                if (checkIsFileOldEnough(f.getChangeTime().toEpochMillis()) && !f.getFileName().equalsIgnoreCase(".") && !f.getFileName().equalsIgnoreCase("..")) {
                    results.add(f.getFileName());
            		}
            }
        } catch (Exception e) {
            throw new SmbConnectionException(ConnectionExceptionCode.UNKNOWN, null, e.getMessage(), e);
        }
        return results;
    }
    
    /**
     * Set the credentials to re-use for connections
     *
     * @return void
     */
    private void setAuthContext() {
		if (!connectorConfig.getGuest() && !connectorConfig.getAnonymous()) {
			ac = new AuthenticationContext(this.getConfig().getUsername(), this.getConfig().getPassword().toCharArray(), this.getConfig().getDomain());
		}
		else if (connectorConfig.getGuest()) {
			this.ac = AuthenticationContext.guest();
	        logger.debug("guest credentials used");
		} else if (connectorConfig.getAnonymous()) {
			this.ac = AuthenticationContext.guest();
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
     * @param String hostname
     * 		hostname to connect to
     * @param String sharename
     * 		sharename to connect to
     * @return DiskShare object
     */
    private DiskShare setShare(String hostname, String sharename) throws IOException {
        Session s = setSession(hostname);
        this.diskShare = (DiskShare) s.connectShare(sharename);
        return this.diskShare;
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
     * @param String hostname
     * 		hostname to connect to
     * @return Session authenticated object
     */
    private Session setSession(String hostname) throws IOException { 		
        if (getSession() == null) {
            Connection c = this.sc.connect(hostname);
            this.smbSession = c.authenticate(ac);
        }
        return this.smbSession;
    }
}
