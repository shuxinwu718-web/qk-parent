package com.qk.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("course")
public class Course {

    private Integer id;

    /**
     * 课程学科编码：1: AI智能应用开发(Java), 2: AI大模型开发(Python), 3: AI鸿蒙开发,
     * 4: AI大数据, 5: AI嵌入式, 6: AI测试, 7: AI运维
     */
    @NotNull(message = "课程学科不能为空")
    @Min(value = 1, message = "课程学科编码无效")
    @Max(value = 7, message = "课程学科编码无效")
    private Integer subject;

    @NotBlank(message = "课程名称不能为空")
    @Size(min = 2, max = 20, message = "课程名称长度需在2-20之间")
    private String name;

    /**
     * 适用人群编码：1: 小白学员, 2: 初级程序员, 3: 中级程序员
     */
    @NotNull(message = "适用人群不能为空")
    @Min(value = 1, message = "适用人群编码无效")
    @Max(value = 3, message = "适用人群编码无效")
    private Integer target;

    @NotNull(message = "课程价格不能为空")
    @Min(value = 1, message = "课程价格最小为1元")
    @Max(value = 99999, message = "课程价格最大为99999元")
    private Integer price;

    @Size(max = 100, message = "课程介绍不能超过100字")
    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}