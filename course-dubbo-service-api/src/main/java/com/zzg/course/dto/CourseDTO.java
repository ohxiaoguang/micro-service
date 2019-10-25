package com.zzg.course.dto;

import com.zzg.thrift.user.dto.TeacherDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author ZZG
 * @Date 2019/10/8 15:49
 * @Version v1.0.0
 */
@Data
public class CourseDTO implements Serializable {

    private int id;
    private String title;
    private String description;
    private TeacherDTO teacher;
}
