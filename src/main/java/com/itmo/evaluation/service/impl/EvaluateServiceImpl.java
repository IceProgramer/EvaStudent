package com.itmo.evaluation.service.impl;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.Lists;

import com.itmo.evaluation.mapper.CourseMapper;
import com.itmo.evaluation.mapper.MarkHistoryMapper;
import com.itmo.evaluation.mapper.SystemMapper;
import com.itmo.evaluation.mapper.TeacherMapper;
import com.itmo.evaluation.model.entity.MarkHistory;
import com.itmo.evaluation.model.entity.System;
import com.itmo.evaluation.model.entity.Teacher;
import com.itmo.evaluation.model.vo.Evaluate.EvaluateChildrenVo;
import com.itmo.evaluation.model.vo.Evaluate.EvaluateTeacherVo;
import com.itmo.evaluation.model.vo.Evaluate.EvaluationVo;
import com.itmo.evaluation.service.EvaluateService;
import com.itmo.evaluation.utils.JwtUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvaluateServiceImpl implements EvaluateService {


    @Resource
    private CourseMapper courseMapper;

    @Resource
    private TeacherMapper teacherMapper;

    @Resource
    private SystemMapper systemMapper;

    @Resource
    private MarkHistoryMapper markHistoryMapper;

    /**
     * 展示该课程下的所有教师和其一级评价
     *
     * @param cid 课程id
     * @return 所有教师信息
     */
    @Override
    public List<EvaluateTeacherVo> listAllTeacher(Integer cid, String token) {
        // 解析token中的studentId
        DecodedJWT decodedJWT = JwtUtil.decodeToken(token);
        Integer studentId = Integer.valueOf(decodedJWT.getClaim("id").asString());


        String courseName = courseMapper.selectById(cid).getCName();
        // 根据课程名称来获取所有的课程id
        List<Integer> courseIdList = courseMapper.getCourseIdByName(courseName);
        // 根据学生id在mark表中根据cid来筛选出本课程的相关信息
        List<MarkHistory> markList = markHistoryMapper.getByStudentId(studentId)
                .stream().filter(markHistory -> courseIdList.contains(markHistory.getCid()))
                .collect(Collectors.toList());

        // 获取该课程的所有老师
        List<Integer> teacherIdList = markList.stream().map(MarkHistory::getTid).distinct().collect(Collectors.toList());
        List<EvaluateTeacherVo> evaluateTeacherVoList = new ArrayList<>();
        for (Integer teacherId : teacherIdList) {
            // 获取老师信息
            EvaluateTeacherVo evaluateTeacherVo = new EvaluateTeacherVo();
            Teacher teacher = teacherMapper.selectById(teacherId);
            evaluateTeacherVo.setTid(teacherId);
            evaluateTeacherVo.setTeacher(teacher.getName());
            evaluateTeacherVo.setIdentity(teacher.getIdentity());

//            evaluateTeacherVo.setEvaluation(Lists.newArrayList());

            // 获取该老师的所有一级评价
            List<MarkHistory> systemList = markList.stream().filter(mark -> mark.getTid().equals(teacherId))
                    .collect(Collectors.toList());
            // 遍历指标
            List<EvaluationVo> evaluationVoList = new ArrayList<>();
            for (MarkHistory mark : systemList) {
                // 一级指标内容
                Integer sid = mark.getSid();
                System system = systemMapper.getSystemById(sid);
                EvaluationVo evaluationVo = new EvaluationVo();
                evaluationVo.setId(sid);
                evaluationVo.setName(system.getName());
                evaluationVo.setEName(system.getEName());
                evaluationVo.setStatus(mark.getState());

                List<System> secondSystemList = systemMapper.getSecondSystemBySid(sid);
                List<EvaluateChildrenVo> evaluateChildrenVoList = new ArrayList<>();

                // 二级指标
                for (System secondSystem : secondSystemList) {
                    EvaluateChildrenVo evaluateChildrenVo = new EvaluateChildrenVo();
                    evaluateChildrenVo.setName(secondSystem.getName());
                    evaluateChildrenVo.setEName(secondSystem.getEName());
                    evaluateChildrenVoList.add(evaluateChildrenVo);
                }
                evaluationVo.setChildren(evaluateChildrenVoList);
                evaluationVoList.add(evaluationVo);
            }
            evaluateTeacherVo.setEvaluation(evaluationVoList);
            evaluateTeacherVoList.add(evaluateTeacherVo);
        }
        return evaluateTeacherVoList;
    }
}
