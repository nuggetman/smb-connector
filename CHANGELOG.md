# SMB Connector Release Notes
# --------------------------------------------------------------------
# Version: 1.2.0
Date: July 2019
Supported driver:
SMBJ v0.9.1
# Supported Mule Runtime Versions:
Mule 3.8+
# New Features and Functionality
* Issue 

# --------------------------------------------------------------------
# Version: 1.1.0-unreleased
Date: June-2019
Supported driver: 
JCIFS v1.3.19
# Supported Mule Runtime Versions: 
Mule 3.8+
# New Features and Functionality
* Issue 6 - Resolved wildcard behaviour 
* Issue 9 - Delete on read behaviour fixed
* Issue 10 - Directory list now returns only files older than the fileage 
# Known Issues in this release

# --------------------------------------------------------------------
# Version: 1.0
Date: Mar-2018
Supported driver: 
JCIFS v1.3.19
# Supported Mule Runtime Versions: 
Mule 3.8+
# New Features and Functionality
Initial version 
# Known Issues in this release
Streaming is currently not supported in DevKit 3.9 (current edition)
The underlying JCIFS library cannot be distributed with the connector, the .jar driver can be downloaded from https://jcifs.samba.org/
JCifs provides no way to “disconnect” from a resource, but it naturally times out after 15 secs of no activity
