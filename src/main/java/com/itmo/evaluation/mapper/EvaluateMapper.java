package com.itmo.evaluation.mapper;

import com.itmo.evaluation.model.entity.Evaluate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
* @author chenjiahan
* @description 针对表【e_evaluate(评测表)】的数据库操作Mapper
* @createDate 2023-01-27 15:01:14
* @Entity com.itmo.evaluation.model.entity.Evaluate
*/
public interface EvaluateMapper extends BaseMapper<Evaluate> {

    @Select("select id from e_evaluate where status = 1")
    Integer getNowEid();

}




