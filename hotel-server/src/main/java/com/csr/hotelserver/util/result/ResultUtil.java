package com.csr.hotelserver.util.result;

public class ResultUtil {

    private static String SUCCESS = "success";
    private static String ERROR = "error";

    public static Result ok(){
        return ok(null);
    }

    public static Result ok(String message){
        return new Result(SUCCESS,message,null);
    }

    public static Result ok(Object body){
        return new Result(SUCCESS,null,body);
    }

    public static Result error(String message){
        return new Result(ERROR,message,null);
    }
}
