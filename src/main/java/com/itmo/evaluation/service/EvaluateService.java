package com.itmo.evaluation.service;

import com.itmo.evaluation.model.vo.Evaluate.EvaluateTeacherVo;

import java.util.List;

/**
 * 学生评测相关
 *
 * @author chenjiahan
 */
public interface EvaluateService {


    /**
     * 展示所有教师和其一级评价
     *
     * @param cid 课程id
     * @return 所有教师信息
     */
    List<EvaluateTeacherVo> listAllTeacher(Integer cid, String token);

}
