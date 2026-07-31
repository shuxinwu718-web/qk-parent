package com.qk.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {

    private Integer id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String email;
    private Integer gender;
    private Integer status;
    private Integer deptId;

    // ✅ 数据库已有 role_id 字段，不需要 @TableField(exist = false)
    private Integer roleId;

    private String image;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 扩展属性（数据库中不存在）
    @TableField(exist = false)
    private String deptName;

    @TableField(exist = false)
    private String roleName;

    // 👇 新增：角色标签（用于登录）
    @TableField(exist = false)
    private String roleLabel;
}