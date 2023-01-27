package com.itmo.evaluation.model.dto.course;

import lombok.Data;

import java.io.Serializable;

/**
 * 获取学生对应课程信息
 */
@Data
public class CourseRequest implements Serializable {
    /**
     * 评测id
     */
    private Integer eid;

    private static final long serialVersionUID = 1L;
}
