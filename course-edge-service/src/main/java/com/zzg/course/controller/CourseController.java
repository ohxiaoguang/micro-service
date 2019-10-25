package com.zzg.course.controller;

import com.zzg.course.dto.CourseDTO;
import com.zzg.course.service.ICourseService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author ZZG
 * @Date 2019/10/8 16:52
 * @Version v1.0.0
 */
@RestController
@RequestMapping("/course")
public class CourseController {

    @Reference
    private ICourseService courseService;

    @GetMapping("/courseList")
    public List<CourseDTO> courseList(){
        return courseService.courseList();
    }
}
