/**
 * Copyright 2018-2020 (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.smb.automation.functional;

public class TestDataBuilder {

    public static final String CANT_DELETE_DIRNAME = new String("cantdeleteme");
    public static final String CANT_DELETE_FILENAME = new String("cantdeleteme.txt");
    public static final String DIR_CREATE_TEST_NAME = new String("newdir");
    public static final String DIR_CREATE_DOUBLE_TEST_NAME = new String("newdoubledir");
    public static final String DIR_LIST_EMPTY_TEST_NAME = new String("newemptydir");
    public static final String DIR_LIST_EMPTY_WILD_TEST_NAME = new String("newemptywilddir");
    public static final String DIR_LIST_FILES_TEST_NAME = new String("newemptylistdir");
    public static final String DIR_LIST_FILES_TEST_FILE_NAME = new String("somefile.txt");
    public static final String DIR_LIST_FILES_WILD_TEST_NAME = new String("newwildlistdir");
    public static final String DIR_LIST_FILES_WILD_TEST_FILE_NAME = new String("somefile.txt");
    public static final String DIR_LIST_FILES_TXT_TEST_NAME = new String("newtxtlistdir");
    public static final String DIR_LIST_FILES_TXT_TEST_FILE_NAME = new String("somefile.txt");
    public static final String DIR_LIST_FILES_BAK_TEST_NAME = new String("newbaklistdir");
    public static final String DIR_LIST_FILES_BAK_TEST_FILE_NAME = new String("somefile.txt");
    public static final String DIR_DELETE_TEST_NAME = new String("deletedir");
    public static final String DIR_DELETE_FAKE_NAME = new String("fakedir");
    public static final String FILE_READ_TEST_FILENAME = new String("fileread_samplefile.txt");
    public static final String FILE_READ_AUTODELETE_TEST_FILENAME = new String("fileread_autodelete_samplefile.txt");
    public static final String FILE_WRITE_TEST_FILENAME = new String("filewrite_samplefile.txt");
    public static final String FILE_WRITE_APPEND_STRING_TEST_FILENAME = new String(
            "filewrite_append_string_samplefile.txt");
    public static final String FILE_WRITE_APPEND_BYTEARRAY_TEST_FILENAME = new String(
            "filewrite_append_bytearray_samplefile.txt");
    public static final String FILE_WRITE_APPEND_INPUTSTREAM_TEST_FILENAME = new String(
            "filewrite_append_instream_samplefile.txt");
    public static final String FILE_DELETE_TEST_FILENAME = new String("filedelete_samplefile.txt");
    public static final String FILE_FAKEDELETE_TEST_FILENAME = new String("filefakedelete_samplefile.txt");
    public static final String FILE_CONTENT = new String("somecontent\n");
    public static final String WILDCARD = new String("*.*");
    public static final String TXTWILDCARD = new String("*.txt");
    public static final String BAKWILDCARD = new String("*.bak");
    public static final String DEFAULT_ENCODING = new String("UTF-8");

}
