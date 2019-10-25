package com.zzg.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author ZZG
 * @Date 2019/10/7 22:49
 * @Version v1.0.0
 */
@Data
@AllArgsConstructor
public class Response implements Serializable {

    public static final Response USERNAME_PASSWORD_INVALID = new Response("1001","username or password invalid");
    public static final Response MOBILE_OR_EMAIL_REQUIRED = new Response("1002","mobile or email is required");
    public static final Response SEND_VERIFYCODE_FAILED = new Response("1003","send verifycode failed");
    public static final Response SUCCESS = new Response("200","success");
    public static final Response VERIFY_CODE_INVALID = new Response("1004","verifyCode invalid");


    public static Response exception(Exception e){
        return new Response("9999",e.getMessage());
    }

    private String code;
    private String message;

}
