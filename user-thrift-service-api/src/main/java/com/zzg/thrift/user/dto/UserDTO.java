package com.zzg.thrift.user.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author ZZG
 * @Date 2019/10/8 0:11
 * @Version v1.0.0
 */
@Data
public class UserDTO implements Serializable {

    private int id;
    private String username;
    private String password;
    private String mobile;
    private String email;
    private String realName;
}
