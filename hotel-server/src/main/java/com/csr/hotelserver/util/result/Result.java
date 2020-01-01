package com.csr.hotelserver.util.result;

import lombok.Data;

@Data
public class Result {
    private String code;
    private String message;
    private Object body;

    public Result(String code, String msg, Object body) {
        this.code = code;
        this.message = msg;
        this.body = body;
    }

}
