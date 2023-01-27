package com.itmo.evaluation.mapper;

import com.itmo.evaluation.model.entity.Student;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
* @author chenjiahan
* @description 针对表【e_student(学生表)】的数据库操作Mapper
* @createDate 2023-01-27 12:23:34
* @Entity com.itmo.evaluation.model.entity.Student
*/
public interface StudentMapper extends BaseMapper<Student> {


    @Select("select * from e_student where sid = #{sid}")
    Student getStudentBySid(String sid);
}




