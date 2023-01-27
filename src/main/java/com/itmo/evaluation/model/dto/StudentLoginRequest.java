package com.itmo.evaluation.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 学生表
 * @TableName e_student
 */
@Data
public class StudentLoginRequest implements Serializable {

    /**
     * 学生账号
     */
    private String username;

    /**
     * 学生密码
     */
    private String password;

    private static final long serialVersionUID = 1L;
}