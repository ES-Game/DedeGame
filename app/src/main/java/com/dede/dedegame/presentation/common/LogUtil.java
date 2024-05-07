package com.dede.dedegame.presentation.common;

import android.content.Context;
import android.util.Log;


public class LogUtil {
    public final int CLASS = 4;
    private static volatile LogUtil instance;

    public static LogUtil getInstance() {
        if (instance == null) {
            instance = new LogUtil();
        }
        return instance;
    }

    public void i(Context context, Object message) {
        Log.i(getClassNamevsMethodvsLineNumber(), message.toString());
    }

    public void d(Object message) {
        Log.d(getClassNamevsMethodvsLineNumber(), message.toString());
    }

    public void e(Object message) {
        Log.e(getClassNamevsMethodvsLineNumber(), message.toString());
    }

    public void v(Object message) {
        Log.v(getClassNamevsMethodvsLineNumber(), message.toString());
    }

    public void w(String message) {
        Log.w(getClassNamevsMethodvsLineNumber(), message);
    }

    public String getClassNamevsMethodvsLineNumber() {
        String fullClassName = Thread.currentThread().getStackTrace()[CLASS].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String methodName = Thread.currentThread().getStackTrace()[CLASS].getMethodName();
        int lineNumber = Thread.currentThread().getStackTrace()[CLASS].getLineNumber();
        return className + "." + methodName + "():" + lineNumber + "----> ";
    }
}
