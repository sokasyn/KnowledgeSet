package com.emin.digit.mobile.android.learning.practiceproject.exception;

/**
 * Created by Samson on 16/6/27.
 */
public class EMDatabaseException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public EMDatabaseException() {
        super();
    }

    public EMDatabaseException(String detailMessage) {
        super(detailMessage);
    }

    public EMDatabaseException(String detailMessage, Throwable throwable) {
        super(detailMessage,throwable);
    }

    public EMDatabaseException(Throwable throwable) {
        super(throwable);
    }
}
