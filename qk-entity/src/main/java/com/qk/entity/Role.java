package com.qk.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("role")
public class Role {

    private Integer id;

    private String name;

    private String label;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    // 扩展字段：是否被当前用户选中（可选）
    @TableField(exist = false)
    private Boolean checked;
}