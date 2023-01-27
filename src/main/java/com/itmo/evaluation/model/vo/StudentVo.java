package com.itmo.evaluation.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 学生表
 * @TableName e_student
 */
@Data
public class StudentVo implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 学号
     */
    private String sid;

    /**
     * 姓名
     */
    private String name;

    private static final long serialVersionUID = 1L;
}