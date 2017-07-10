package com.github.mkopylec.errorest;

public class Error {

    protected final String code;
    protected final String description;

    public Error(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
