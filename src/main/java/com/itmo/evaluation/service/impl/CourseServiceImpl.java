package com.itmo.evaluation.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itmo.evaluation.mapper.CourseMapper;
import com.itmo.evaluation.mapper.MarkHistoryMapper;
import com.itmo.evaluation.model.entity.Course;
import com.itmo.evaluation.model.entity.MarkHistory;
import com.itmo.evaluation.model.vo.CourseVo;
import com.itmo.evaluation.service.CourseService;
import com.itmo.evaluation.utils.JwtUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Resource
    private MarkHistoryMapper markHistoryMapper;

    @Resource
    private CourseMapper courseMapper;


    /**
     * 获取当前未完成课程
     *
     * @param token token
     * @param eid   评测id
     * @return 完成课程
     */
    @Override
    public List<CourseVo> getUndoneCourse(String token, Integer eid) {
        // 解析出学生id
        DecodedJWT decodedJWT = JwtUtil.decodeToken(token);
        Integer studentId = Integer.valueOf(decodedJWT.getClaim("id").asString());

        List<CourseVo> courseVoList = new ArrayList<>();

        // 学生需要评价的所有课程信息 【包含老师和所有一级评价】
        List<MarkHistory> course = markHistoryMapper.getByEidAndStudentId(eid, studentId);
        List<Integer> courseList = course.stream().map(MarkHistory::getCid).distinct().collect(Collectors.toList());

        for (Integer courseId : courseList) {
            // 获取当前课程的所有回答状态
            List<Integer> stateList = course.stream().filter(single -> Objects.equals(single.getCid(), courseId)).map(MarkHistory::getState).collect(Collectors.toList());
            // 若该课程中有一项一级指标的state为0，说明该课程评价未完成
            if (stateList.contains(0)) {
                Course courseInfo = courseMapper.selectById(courseId);
                CourseVo courseVo = new CourseVo();
                courseVo.setCid(courseId);
                courseVo.setCName(courseInfo.getCName());
                courseVo.setEName(courseInfo.getEName());
                courseVoList.add(courseVo);
            }
        }
        List<CourseVo> courseInfoList = new ArrayList<>();
        courseInfoList.add(courseVoList.get(0));
        for (int i = 1; i < courseVoList.size(); i++) {
            if (!Objects.equals(courseVoList.get(i).getCName(), courseVoList.get(i - 1).getCName())) {
                courseInfoList.add(courseVoList.get(i));
            }
        }
        return courseInfoList;
    }
}
