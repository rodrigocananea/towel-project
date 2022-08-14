package com.towel.login;

public class CannotLoginException extends Exception {
    private String msg;

    public CannotLoginException(String msg2) {
        this.msg = msg2;
    }

    public String getMessage() {
        return this.msg;
    }
}
