package com.itmo.evaluation.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CourseVo implements Serializable {

    /**
     * 课程id
     */
    private Integer cid;

    /**
     * 课程中文名
     */
    private String cName;

    /**
     * 课程中文名
     */
    private String eName;

    private static final long serialVersionUID = 1L;
}
