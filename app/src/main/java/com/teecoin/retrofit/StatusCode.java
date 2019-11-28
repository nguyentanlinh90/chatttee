package com.teecoin.retrofit;

public enum StatusCode {

    ERR_NO_INTERNET_CONNECTION(1),
    ERR_UNAUTHORIZED(2),
    ERR_API_CALL_FAIL(3),
    TIMEOUT_CONNECTION(4),
    BAD_REQUEST(400),
    FORCE_LOGIN(401),
    NOT_FOUND(404),
    SERVICE_UNAVAILABLE(503),
    INTERNAL_SERVER_ERROR(500);

    private int value;

    StatusCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
