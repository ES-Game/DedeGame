package com.dede.dedegame.repo.network;

import androidx.annotation.Nullable;

import com.quangph.base.mvp.action.ActionException;

public class APIActionException extends ActionException {
    private int code;
    private String message;

    public APIActionException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @Nullable
    @Override
    public String getMessage() {
        return message;
    }

    public boolean isExpired() {
        return code == 800;
    }

    public boolean isRefreshTokenExpired() {
        return code == 801;
    }
}
