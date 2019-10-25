package com.zzg.user.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author ZZG
 * @Date 2019/10/8 0:14
 * @Version v1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginResponse extends Response{

    private String token;
    public LoginResponse(String token){
        super("200","login success");
        this.token = token;
    }

}
