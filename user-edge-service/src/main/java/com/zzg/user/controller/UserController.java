package com.zzg.user.controller;

import com.zzg.thrift.user.UserInfo;
import com.zzg.thrift.user.dto.UserDTO;
import com.zzg.user.ServiceApplication;
import com.zzg.user.redis.RedisClient;
import com.zzg.user.response.LoginResponse;
import com.zzg.user.response.Response;
import com.zzg.user.thrift.ServiceProvider;
import org.apache.commons.lang.StringUtils;
import org.apache.thrift.TException;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.Random;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

/**
 * @Author ZZG
 * @Date 2019/10/7 22:36
 * @Version v1.0.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private ServiceProvider serviceProvider;
    @Resource
    private RedisClient redisClient;

    @PostMapping("/login")
    public Response login(String username, String password){
        //1. 验证用户名密码
        UserInfo userInfo = null;
        try {
            userInfo = serviceProvider.getUserService().getUserByName(username);
        } catch (TException e) {
            e.printStackTrace();
            return Response.USERNAME_PASSWORD_INVALID;
        }
        if (userInfo == null) {
            return Response.USERNAME_PASSWORD_INVALID;
        }
        if (!userInfo.getPassword().equals(md5Hex(password))){
            return Response.USERNAME_PASSWORD_INVALID;
        }

        //2. 生成token
        String token = genToken();
        //3. 缓存用户
        redisClient.set(token,toDTO(userInfo),3600);
        return new LoginResponse(token);
    }

    @PostMapping("/sendVerifyCode")
    public Response sendVerifyCode(@RequestParam(value = "mobile",required = false) String mobile,
                                   @RequestParam(value = "email",required = false)String email){
        String message = "Verify code is:";
        String code = randomCode("0123456789",6);
        try {
            boolean result =false;
            if (StringUtils.isNotBlank(mobile)){
                result = serviceProvider.getMessageService().sendMobileMessage(mobile,message+code);
                redisClient.set(mobile,code);
            }else if (StringUtils.isNotBlank(email)){
                result = serviceProvider.getMessageService().sendEmailMessage(email,message+code);
                redisClient.set(email,code);
            }else {
                return Response.MOBILE_OR_EMAIL_REQUIRED;
            }
            if (!result){
                return Response.SEND_VERIFYCODE_FAILED;
            }
        } catch (TException e) {
            e.printStackTrace();
            return Response.exception(e);
        }
        return Response.SUCCESS;
    }

    @PostMapping("/register")
    public Response register(String username, String password,
                             @RequestParam(value = "mobile",required = false) String mobile,
                             @RequestParam(value = "email",required = false)String email, String verifyCode){
        if (StringUtils.isBlank(mobile) && StringUtils.isBlank(email)){
            return Response.MOBILE_OR_EMAIL_REQUIRED;
        }

        if (StringUtils.isNotBlank(mobile)){
            String redisCode = redisClient.get(mobile);
            if (!verifyCode.equals(redisCode)) {
                return Response.VERIFY_CODE_INVALID;
            }
        }else {
            String redisCode = redisClient.get(email);
            if (!verifyCode.equals(redisCode)) {
                return Response.VERIFY_CODE_INVALID;
            }
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(md5Hex(password));
        userInfo.setMobile(mobile);
        userInfo.setEmail(email);

        try {
            serviceProvider.getUserService().registerUser(userInfo);
        } catch (TException e) {
            e.printStackTrace();
            return Response.exception(e);
        }

        return Response.SUCCESS;
    }

    @PostMapping("/authentication")
    public UserDTO authentication(@RequestHeader String token){
        return redisClient.get(token);
    }


    private UserDTO toDTO(UserInfo userInfo) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userInfo,userDTO);
        return userDTO;
    }

    private String genToken() {
        return randomCode("0123456789abcdefghijklmnopqrstuvwxyz",32);
    }

    private String randomCode(String s, int size) {
        StringBuilder result = new StringBuilder(size);
        Random random = new Random();
        for (int i = 0; i <size ; i++) {
            int loc = random.nextInt(s.length());
            result.append(s.charAt(loc));
        }
        return result.toString();
    }
}
