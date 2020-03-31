package org.mule.extension.smb.exception;

public enum SmbConnectionExceptionCode {

    CANNOT_REACH("Unreachable host"),
    READ_ERROR("Error reading data"),
    WRITE_ERROR("Error writing data"),
    UNKNOWN_HOST("Unknown host"),
    UNKNOWN("Unknown error");

    private final String code;

    private SmbConnectionExceptionCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
