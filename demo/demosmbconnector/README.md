SMB Connector Demo Application
==============================

Description:
Based on the API input this demo application performs the following -

* /directory/{directory_name}
	get: list a directory contents
    post: create a directory
    delete: delete a directory 
* /file/{file_name}:
    get: get file contents
    post: write file contents to a new file
    put: append file contents

To be able to successfully run this application, config values must be set in mule-app.properties file:
* domain
* host
* path
* username
* password
* timeout

Usage:
Once mule is up and running you can call http://localhost:8081/console to get the API console and interact with the API