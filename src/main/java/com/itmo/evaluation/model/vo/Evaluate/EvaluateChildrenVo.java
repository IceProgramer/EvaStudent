package com.itmo.evaluation.model.vo.Evaluate;

import lombok.Data;

import java.io.Serializable;

@Data
public class EvaluateChildrenVo implements Serializable {

    /**
     * 二级评价名称
     */
    private String name;

    /**
     * 二级评价英文名
     */
    private String eName;

    private static final long serialVersionUID = 1L;

}
