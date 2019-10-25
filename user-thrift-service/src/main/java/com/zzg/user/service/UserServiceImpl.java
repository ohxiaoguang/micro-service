package com.zzg.user.service;

import com.zzg.thrift.user.UserInfo;
import com.zzg.thrift.user.UserService;
import com.zzg.user.mapper.UserMapper;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author ZZG
 * @Date 2019/10/7 21:36
 * @Version v1.0.0
 */
@Service
public class UserServiceImpl implements UserService.Iface {

    @Resource
    private UserMapper userMapper;
    @Override
    public UserInfo getUserById(int id) throws TException {
        return userMapper.getUserById(id);
    }

    @Override
    public UserInfo getTeacherById(int id) throws TException {
        return userMapper.getTeacherById(id);
    }

    @Override
    public UserInfo getUserByName(String username) throws TException {
        return userMapper.getUserByName(username);
    }

    @Override
    public void registerUser(UserInfo userInfo) throws TException {
        userMapper.registerUser(userInfo);
    }
}
