package com.emin.digit.mobile.android.database.exception;

/**
 * Created by Samson on 16/7/4.
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
