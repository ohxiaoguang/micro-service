package com.zzg.course.mapper;

import com.zzg.course.dto.CourseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author ZZG
 * @Date 2019/10/8 16:09
 * @Version v1.0.0
 */
@Mapper
public interface CourseMapper {

    @Select("select * from pe_course")
    List<CourseDTO> listCourse();

    @Select("select user_id from pr_user_course where course_id=#{courseId} ")
    Integer getCourseTeacher(@Param("courseId")int courseId);
}
