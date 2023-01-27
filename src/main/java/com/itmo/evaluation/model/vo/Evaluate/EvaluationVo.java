package com.itmo.evaluation.model.vo.Evaluate;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EvaluationVo implements Serializable {

    /**
     * 一级评测id
     */
    private Integer id;

    /**
     * 一级评测名称
     */
    private String name;

    /**
     * 一级评测英文名
     */
    private String eName;

    /**
     * 评测状态
     */
    private Integer status;

    /**
     * 二级评价名称
     */
    private List<EvaluateChildrenVo> children;

    private static final long serialVersionUID = 1L;
}
