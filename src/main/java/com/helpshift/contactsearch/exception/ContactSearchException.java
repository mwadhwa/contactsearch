package com.helpshift.contactsearch.exception;

import lombok.Getter;

/**
 * Created by milap.wadhwa.
 */
@Getter
public class ContactSearchException extends Exception {

    private ErrorCode errorCode;

    public ContactSearchException(String message,
        ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public enum ErrorCode
    {
        EMPTY_TEXT,
        INVALID_INPUT;
    }
}
