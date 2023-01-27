package com.itmo.evaluation;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itmo.evaluation.mapper.CourseMapper;
import com.itmo.evaluation.mapper.MarkHistoryMapper;
import com.itmo.evaluation.model.entity.MarkHistory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class evaluationStudentApplicationTests {

    @Resource
    private MarkHistoryMapper markHistoryMapper;

    @Resource
    private CourseMapper courseMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void demo() {
        Integer cid = 1;
        Integer studentId = 3;
        String courseName = courseMapper.selectById(cid).getCName();
        List<Integer> courseId = courseMapper.getCourseIdByName(courseName);
        // 根据课程名称来获取所有的课程名称
        LambdaQueryWrapper<MarkHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MarkHistory::getAid, studentId);
        List<MarkHistory> markHistories = markHistoryMapper.selectList(queryWrapper);
        List<MarkHistory> collect = markHistories.stream().filter(markHistory -> courseId.contains(markHistory.getCid())).collect(Collectors.toList());
        System.out.println(collect);
    }

    @Test
    void push() {


        // 每个cid一定相同
        Integer cid = 1;
        String courseName = courseMapper.selectById(cid).getCName();

        // 根据课程名称来获取所有的课程id 【例如Java有可能是1，2，3】
        List<Integer> courseIdList = courseMapper.getCourseIdByName(courseName);


        Integer studentId = 3;
        Integer sid = 1;
        Integer score = 43;
        Integer eid = 1;
        Integer tid = 1;

        List<MarkHistory> markHistoryList = markHistoryMapper.getByAidAndSidAndEidAndTid(studentId, sid, eid, tid);
        // 找到与课程匹配的记录
        List<MarkHistory> markHistories = markHistoryList.stream()
                .filter(markHistory -> courseIdList.contains(markHistory.getCid()))
                .collect(Collectors.toList());

        MarkHistory markHistory = markHistories.get(0);
        markHistory.setScore(score);
        markHistory.setState(1);
        System.out.println(markHistory);
    }

}
