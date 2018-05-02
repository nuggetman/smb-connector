# SMB Connector Release Notes
# --------------------------------------------------------------------
Date: Mar-2018
Version: 1.0
Supported driver: 
JCIFS v1.3.19
# Supported Mule Runtime Versions: 
${project.devkitVersion}
# New Features and Functionality
Initial version 
# Known Issues in this release
Streaming is currently not supported in DevKit 3.9 (current edition)
The underlying JCIFS library cannot be distributed with the connector, the .jar driver can be downloaded from https://jcifs.samba.org/
JCifs provides no way to “disconnect” from a resource, but it naturally times out after 15 secs of no activity