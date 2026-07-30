package com.qk.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("dept")
public class Dept {

    @NotNull(message = "部门ID不能为空")
    private Integer id;

    @NotBlank(message = "部门名称不能为空")
    @TableField("name")
    @JsonProperty("name")  // 👈 让 JSON 返回 name，不是 deptName
    private String deptName;

    private Integer status;

    @JsonProperty("createTime")
    private LocalDateTime createTime;

    @JsonProperty("updateTime")
    private LocalDateTime updateTime;
}