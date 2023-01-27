package com.itmo.evaluation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itmo.evaluation.model.entity.Course;
import com.itmo.evaluation.model.vo.CourseVo;
import com.itmo.evaluation.model.vo.EvaluateIdVo;

import java.util.List;

public interface CourseService extends IService<Course> {

    /**
     * 获取当前未完成课程
     *
     * @param token token
     * @param eid   评测id
     * @return 完成课程
     */
    List<CourseVo> getUndoneCourse(String token, Integer eid);

    /**
     * 获取当前已完成课程
     *
     * @param token token
     * @param eid   评测id
     * @return 完成课程
     */
    List<CourseVo> getDoneCourse(String token, Integer eid);

    EvaluateIdVo getNowEid();
}
