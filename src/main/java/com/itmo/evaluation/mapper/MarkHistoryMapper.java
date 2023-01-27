package com.itmo.evaluation.mapper;

import com.itmo.evaluation.model.entity.MarkHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author chenjiahan
* @description 针对表【e_mark_history(一级指标表)】的数据库操作Mapper
* @createDate 2023-01-27 13:26:26
* @Entity com.itmo.evaluation.model.entity.MarkHistory
*/
public interface MarkHistoryMapper extends BaseMapper<MarkHistory> {

    /**
     * 根据评测和学生获取
     * @param eid 评测id
     * @param studentId 学生id
     * @return 评价内容
     */
    @Select("select * from e_mark_history where eid = #{eid} and aid = #{studentId}")
    List<MarkHistory> getByEidAndStudentId(Integer eid, Integer studentId);

    @Select("select * from e_mark_history where aid = #{studentId}")
    List<MarkHistory> getByStudentId(Integer studentId);
}




