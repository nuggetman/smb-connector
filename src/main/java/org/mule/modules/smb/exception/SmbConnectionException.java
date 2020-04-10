/**
 * Copyright 2018-2020 (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.smb.exception;

import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;

public class SmbConnectionException extends ConnectionException {

    private static final long serialVersionUID = 1L;

    public static final String READ_ERROR = "READ ERROR";
    public static final String WRITE_ERROR = "WRITE ERROR";

    public SmbConnectionException(ConnectionExceptionCode code, String thirdPartyCode, String message,
            Throwable throwable) {
        super(code, thirdPartyCode, message, throwable);
    }
}