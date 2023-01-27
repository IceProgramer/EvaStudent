package com.itmo.evaluation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itmo.evaluation.model.entity.Student;
import com.itmo.evaluation.service.StudentService;
import com.itmo.evaluation.mapper.StudentMapper;
import org.springframework.stereotype.Service;

/**
* @author chenjiahan
* @description 针对表【e_student(学生表)】的数据库操作Service实现
* @createDate 2023-01-27 12:23:34
*/
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student>
    implements StudentService{

}




