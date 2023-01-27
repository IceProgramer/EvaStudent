package com.itmo.evaluation.common;

import lombok.Data;

import java.io.Serializable;

/**
 * id请求
 *
 * @author chenjiahan
 */
@Data
public class IdRequest implements Serializable {
    /**
     * id
     */
    private Integer id;

    private static final long serialVersionUID = 1L;
}