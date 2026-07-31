package com.qk.dto;

import lombok.Data;

/**
 * 线索查询参数封装类
 */
@Data
public class ClueQueryDto {

    /**
     * 线索ID
     */
    private Integer clueId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 线索状态，1:待分配, 2:跟进中, 3:已关闭, 4:伪线索
     */
    private Integer status;

    /**
     * 线索来源，1:线上活动, 2:推广介绍
     */
    private Integer channel;

    /**
     * 线索归属人
     */
    private String assignName;

    /**
     * 分页查询的页码，如果未指定，默认为1
     */
    private Integer page = 1;

    /**
     * 分页查询的每页记录数，如果未指定，默认为10
     */
    private Integer pageSize = 10;
}