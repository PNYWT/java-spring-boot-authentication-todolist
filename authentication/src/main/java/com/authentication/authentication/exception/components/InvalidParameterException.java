package com.authentication.authentication.exception.components;

public class InvalidParameterException extends RuntimeException {
    private final String invalidParam;
    private final String suggestedParam;

    public InvalidParameterException(String invalidParam, String suggestedParam) {
        super("Unexpected parameter: " + invalidParam);
        this.invalidParam = invalidParam;
        this.suggestedParam = suggestedParam;
    }

    public String getInvalidParam() {
        return invalidParam;
    }

    public String getSuggestedParam() {
        return suggestedParam;
    }
}
