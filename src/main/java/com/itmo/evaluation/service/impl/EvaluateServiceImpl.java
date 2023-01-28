package com.itmo.evaluation.service.impl;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itmo.evaluation.common.ErrorCode;
import com.itmo.evaluation.common.ResultUtils;
import com.itmo.evaluation.exception.BusinessException;
import com.itmo.evaluation.mapper.CourseMapper;
import com.itmo.evaluation.mapper.MarkHistoryMapper;
import com.itmo.evaluation.mapper.SystemMapper;
import com.itmo.evaluation.mapper.TeacherMapper;
import com.itmo.evaluation.model.dto.evaluation.EvaluationPushRequest;
import com.itmo.evaluation.model.entity.Course;
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

        Course course = courseMapper.selectById(cid);

        if (course == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "您没有该课程信息");
        }

        String courseName = course.getCName();

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

    @Override
    public Boolean pushEvaluation(List<EvaluationPushRequest> evaluationPushRequestList, String token) {
        // 解析token中的studentId
        DecodedJWT decodedJWT = JwtUtil.decodeToken(token);
        Integer studentId = Integer.valueOf(decodedJWT.getClaim("id").asString());

        // 每个cid一定相同
        Integer cid = evaluationPushRequestList.get(0).getCid();
        String courseName = courseMapper.selectById(cid).getCName();

        // 根据课程名称来获取所有的课程id 【例如Java有可能是1，2，3】
        List<Integer> courseIdList = courseMapper.getCourseIdByName(courseName);

        // 遍历课程中的每一个教师信息 [tid, sid, score, eid, cid]
        for (EvaluationPushRequest evaluationPushRequest : evaluationPushRequestList) {
            Integer sid = evaluationPushRequest.getSid();   // 一级评价id
            Integer score = evaluationPushRequest.getScore();   // 一级评价分数
            Integer eid = evaluationPushRequest.getEid();   // 本次eid
            Integer tid = evaluationPushRequest.getTid();   // 教师id

            List<MarkHistory> markHistoryList = markHistoryMapper.getByAidAndSidAndEidAndTid(studentId, sid, eid, tid);
            // 找到与课程匹配的记录
            List<MarkHistory> markHistories = markHistoryList.stream()
                    .filter(markHistory -> courseIdList.contains(markHistory.getCid()))
                    .collect(Collectors.toList());

            MarkHistory markHistory = markHistories.get(0);
            markHistory.setScore(score);
            markHistory.setState(1);
            markHistoryMapper.updateById(markHistory);
        }
        return true;
    }

}
