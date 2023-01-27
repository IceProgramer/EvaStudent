package com.itmo.evaluation.model.dto.evaluation;

import lombok.Data;

import java.io.Serializable;

@Data
public class EvaluationRequest implements Serializable {

    /**
     * 课程id
     */
    private Integer cid;

    private static final long serialVersionUID = 1L;
}
