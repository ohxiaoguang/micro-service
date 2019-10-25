package com.zzg.course.filter;

import com.zzg.thrift.user.dto.UserDTO;
import com.zzg.user.client.LoginFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author ZZG
 * @Date 2019/10/8 16:59
 * @Version v1.0.0
 */
public class CourseFilter extends LoginFilter {
    @Override
    protected void login(HttpServletRequest request, HttpServletResponse response, UserDTO userDTO) {
        System.out.println("CourseFilter: "+userDTO.getUsername());
    }
}
