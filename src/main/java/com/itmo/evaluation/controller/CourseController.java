package com.itmo.evaluation.controller;

import com.itmo.evaluation.common.BaseResponse;
import com.itmo.evaluation.common.ErrorCode;
import com.itmo.evaluation.common.ResultUtils;
import com.itmo.evaluation.exception.BusinessException;
import com.itmo.evaluation.model.dto.course.CourseRequest;
import com.itmo.evaluation.model.vo.CourseVo;
import com.itmo.evaluation.model.vo.EvaluateIdVo;
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

    /**
     * 获取学生未完成的课程评价
     * @param token token
     * @param courseRequest 请求体
     * @return 未完成课程信息
     */
    @PostMapping("/list/undone")
    public BaseResponse<List<CourseVo>> getUndoneCourse(@RequestHeader("token") String token, @RequestBody CourseRequest courseRequest) {
        Integer eid = courseRequest.getEid();
        if (eid == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<CourseVo> undoneCourse = courseService.getUndoneCourse(token, eid);

        return ResultUtils.success(undoneCourse);
    }

    /**
     * 获取学生已完成的课程评价
     * @param token token
     * @param courseRequest 请求体
     * @return 已完成课程信息
     */
    @PostMapping("/list/done")
    public BaseResponse<List<CourseVo>> getDoneCourse(@RequestHeader("token") String token, @RequestBody CourseRequest courseRequest) {
        Integer eid = courseRequest.getEid();
        if (eid == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<CourseVo> doneCourse = courseService.getDoneCourse(token, eid);

        return ResultUtils.success(doneCourse);
    }

    /**
     * 获取当前正在进行的评测id
     *
     */
    @GetMapping("/getNowEid")
    public BaseResponse<EvaluateIdVo> getNowEid(@RequestHeader("token") String token) {
        EvaluateIdVo evaluateIdVo = courseService.getNowEid();
        if (evaluateIdVo == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }

        return ResultUtils.success(evaluateIdVo);
    }
}
