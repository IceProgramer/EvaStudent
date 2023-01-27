package com.itmo.evaluation.model.vo.Evaluate;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EvaluateTeacherVo implements Serializable {

    /**
     * 教师id
     */
    private Integer tid;

    /**
     * 教师名称
     */
    private String teacher;

    /**
     * 国籍
     */
    private Integer identity;

    /**
     * 一级评价细则
     */
    private List<EvaluationVo> evaluation;

    private static final long serialVersionUID = 1L;

}
