package com.itmo.evaluation.model.dto.evaluation;

import lombok.Data;

import java.io.Serializable;

@Data
public class EvaluationPushRequest implements Serializable {

    /**
     * 课程主键
     */
    private Integer cid;

    /**
     * 教师主键
     */
    private Integer tid;

    /**
     * 一级评价主键
     */
    private Integer sid;

    /**
     * 一级评价分数
     */
    private Integer score;

    /**
     * 当前开放的评价的主键
     */
    private Integer eid;

    private static final long serialVersionUID = 1L;
}
