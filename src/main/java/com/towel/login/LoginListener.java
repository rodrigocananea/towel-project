package com.towel.login;

public interface LoginListener {
    void close();

    User login(String str, String str2) throws CannotLoginException;
}
