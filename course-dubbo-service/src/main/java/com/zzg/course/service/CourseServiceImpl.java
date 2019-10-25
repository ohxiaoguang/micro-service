package com.zzg.course.service;

import com.zzg.course.dto.CourseDTO;
import com.zzg.course.mapper.CourseMapper;
import com.zzg.course.thrift.ServiceProvider;
import com.zzg.thrift.user.UserInfo;
import com.zzg.thrift.user.dto.TeacherDTO;
import org.apache.dubbo.config.annotation.Service;
import org.apache.thrift.TException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

/**
 * @Author ZZG
 * @Date 2019/10/8 16:05
 * @Version v1.0.0
 */
@Service
public class CourseServiceImpl implements ICourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ServiceProvider serviceProvider;

    @Override
    public List<CourseDTO> courseList() {

        List<CourseDTO> courseDTOS = courseMapper.listCourse();
        if (courseDTOS != null) {
            courseDTOS.forEach(courseDTO -> {
                Integer teacherId = courseMapper.getCourseTeacher(courseDTO.getId());
                if (teacherId != null) {
                    try {
                        UserInfo teacherInfo = serviceProvider.getUserService().getTeacherById(teacherId);
                        courseDTO.setTeacher(trans2Teacher(teacherInfo));
                    } catch (TException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        return courseDTOS;
    }

    private TeacherDTO trans2Teacher(UserInfo teacherInfo) {
        TeacherDTO teacherDTO = new TeacherDTO();
        BeanUtils.copyProperties(teacherInfo,teacherDTO);
        return teacherDTO;
    }
}
