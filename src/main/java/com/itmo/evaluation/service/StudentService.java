package com.itmo.evaluation.service;

import com.itmo.evaluation.model.dto.student.StudentLoginRequest;
import com.itmo.evaluation.model.entity.Student;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itmo.evaluation.model.vo.StudentVo;

import javax.servlet.http.HttpServletRequest;

/**
* @author chenjiahan
* @description 针对表【e_student(学生表)】的数据库操作Service
* @createDate 2023-01-27 12:23:34
*/
public interface StudentService extends IService<Student> {

    /**
     * 学生登陆
     */
    String studentLogin(StudentLoginRequest studentLoginRequest, HttpServletRequest request);

    /**
     * 获取登陆学生信息
     */
    StudentVo getLoginStudent(String token);

    /**
     * 获取用户ip地址
     * @param request 网路请求
     * @return ip地址
     */
    String getIpAddr(HttpServletRequest request);

}
