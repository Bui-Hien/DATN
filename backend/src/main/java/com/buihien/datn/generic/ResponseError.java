package com.buihien.datn.generic;

public class ResponseError extends ResponseData {

    public ResponseError(int status, String message) {
        super(status, message);
    }
}