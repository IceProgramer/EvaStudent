package com.itmo.evaluation.mapper;

import com.itmo.evaluation.model.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author chenjiahan
* @description 针对表【e_course(课程表)】的数据库操作Mapper
* @createDate 2023-01-27 13:30:43
* @Entity com.itmo.evaluation.model.entity.Course
*/
public interface CourseMapper extends BaseMapper<Course> {

    @Select("select tid from e_course where cName = name")
    List<Integer> getTeacherIdByName(String name);

    @Select("select id from e_course where cName =#{name}")
    List<Integer> getCourseIdByName(String name);

}




