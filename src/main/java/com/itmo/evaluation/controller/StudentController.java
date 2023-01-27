package com.itmo.evaluation.controller;

import com.itmo.evaluation.common.BaseResponse;
import com.itmo.evaluation.common.ErrorCode;
import com.itmo.evaluation.common.ResultUtils;
import com.itmo.evaluation.exception.BusinessException;
import com.itmo.evaluation.model.dto.StudentLoginRequest;
import com.itmo.evaluation.model.vo.StudentVo;
import com.itmo.evaluation.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 学生登陆相关接口
 *
 */
@Slf4j
@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    private StudentService studentService;

    /**
     * 学生登陆
     * @param studentLoginRequest 学生登陆请求体
     * @param request 请求
     * @return token
     */
    @PostMapping("/login")
    public BaseResponse<String> studentLogin(@RequestBody StudentLoginRequest studentLoginRequest, HttpServletRequest request) {
        if (studentLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }

        String token = studentService.studentLogin(studentLoginRequest, request);

        if (token == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "生成token失败");
        }

        return ResultUtils.success(token);
    }

    /**
     * 获取用户登陆信息
     * @param token token
     * @return 学生信息
     */
    @GetMapping("/get/login")
    public BaseResponse<StudentVo> getLoginStudent(@RequestHeader("token") String token) {
        StudentVo studentInfo = studentService.getLoginStudent(token);
        if (studentInfo == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "用户信息不存在");
        }
        return ResultUtils.success(studentInfo);
    }


}
