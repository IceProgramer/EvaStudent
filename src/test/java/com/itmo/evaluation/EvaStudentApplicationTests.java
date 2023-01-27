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

}
