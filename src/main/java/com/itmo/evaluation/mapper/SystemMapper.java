package com.itmo.evaluation.mapper;

import com.itmo.evaluation.model.entity.System;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author chenjiahan
* @description 针对表【e_system(评价体系表)】的数据库操作Mapper
* @createDate 2023-01-27 16:04:43
* @Entity com.itmo.evaluation.model.entity.System
*/
public interface SystemMapper extends BaseMapper<System> {

    /**
     * 根据国籍获取教师一级评价
     * @param kind 国籍
     * @return 一级评价
     */
    @Select("select * from e_system where level = 1 and kind = #{kind}")
    List<System> getSystemByKind(Integer kind);

    @Select("select * from e_system where level = 1 and id = #{sid}")
    System getSystemById(Integer sid);

    @Select("select * from e_system where level = 2 and sid = #{sid}")
    List<System> getSecondSystemBySid(Integer sid);

}




