package com.quangph.jetpack;

/**
 * Created by Pham Hai Quang on 10/08/2019.
 */
public class JetException extends Exception {

    private int mCode;
    private Object mPayLoad;

    public JetException() {
        super();
    }

    public JetException(int code) {
        super();
        mCode = code;
    }

    public JetException(String message) {
        super(message);
    }

    public JetException(String message, int code) {
        super(message);
        mCode = code;
    }

    public JetException(String message, int code, Object payload) {
        super(message);
        mCode = code;
        mPayLoad = payload;
    }

    public JetException(String message, Throwable t) {
        super(message, t);
    }

    public JetException(int code, Throwable t) {
        super(t);
        mCode = code;
    }

    public JetException(Throwable t) {
        super(t);
    }

    public int getCode() {
        return mCode;
    }

    public Object getPayload() {
        return mPayLoad;
    }
}
