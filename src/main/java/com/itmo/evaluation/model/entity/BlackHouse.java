package com.itmo.evaluation.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 小黑屋
 * @TableName e_black_house
 */
@TableName(value ="e_black_house")
@Data
public class BlackHouse implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * ip地址
     */
    private String ip;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}