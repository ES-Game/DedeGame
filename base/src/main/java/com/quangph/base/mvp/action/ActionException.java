package com.quangph.base.mvp.action;

/**
 * Created by Pham Hai Quang on 1/4/2019.
 */
public class ActionException extends Exception {
    public ActionException() {
        super();
    }

    public ActionException(String message) {
        super(message);
    }

    public ActionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActionException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
