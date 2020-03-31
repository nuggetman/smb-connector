/**
 * Copyright 2018-2020 (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.extension.smb.exception;


public class SmbConnectionException extends Throwable {

    private static final long serialVersionUID = 1L;

    public SmbConnectionException(SmbConnectionExceptionCode c, String message,
                                  Throwable throwable) {
        //super(thirdPartyCode, message, throwable);
    }
}