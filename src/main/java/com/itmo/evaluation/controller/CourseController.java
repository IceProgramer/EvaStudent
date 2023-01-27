package com.itmo.evaluation.controller;

import com.itmo.evaluation.common.BaseResponse;
import com.itmo.evaluation.common.ErrorCode;
import com.itmo.evaluation.common.ResultUtils;
import com.itmo.evaluation.exception.BusinessException;
import com.itmo.evaluation.model.dto.course.CourseRequest;
import com.itmo.evaluation.model.vo.CourseVo;
import com.itmo.evaluation.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 展示课程相关接口
 */
@RestController
@Slf4j
@RequestMapping("/evaluation")
public class CourseController {


    @Resource
    private CourseService courseService;

    @PostMapping("/list/undone")
    public BaseResponse<List<CourseVo>> getUndoneCourse(@RequestHeader("token") String token, @RequestBody CourseRequest courseRequest) {
        Integer eid = courseRequest.getEid();
        if (eid == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<CourseVo> undoneCourse = courseService.getUndoneCourse(token, eid);

        return ResultUtils.success(undoneCourse);
    }
}
