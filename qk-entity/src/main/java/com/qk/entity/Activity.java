package com.qk.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("activity")
public class Activity {

    private Integer id;

    /**
     * 渠道来源：1: 线上活动, 2: 推广介绍
     */
    @NotNull(message = "渠道来源不能为空")
    @Min(value = 1, message = "渠道来源编码无效")
    @Max(value = 2, message = "渠道来源编码无效")
    private Integer channel;

    @NotBlank(message = "活动名称不能为空")
    @Size(min = 1, max = 20, message = "活动名称长度需在1-20之间")
    private String name;

    /**
     * 活动类型：1: 课程折扣, 2: 代金券
     */
    @NotNull(message = "活动类型不能为空")
    @Min(value = 1, message = "活动类型编码无效")
    @Max(value = 2, message = "活动类型编码无效")
    private Integer type;

    /**
     * 课程折扣，如 8.8（type=1 课程折扣时必填）
     */
    @DecimalMin(value = "0.1", message = "课程折扣范围需在0.1-9.9之间")
    @DecimalMax(value = "9.9", message = "课程折扣范围需在0.1-9.9之间")
    private BigDecimal discount;

    /**
     * 代金券金额（元），如 500（type=2 代金券时必填）
     */
    @Min(value = 1, message = "代金券金额最小为1元")
    @Max(value = 99999, message = "代金券金额最大为99999元")
    private Integer voucher;

    @NotBlank(message = "活动简介不能为空")
    @Size(min = 5, max = 100, message = "活动简介长度需在5-100字之间")
    private String description;

    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
