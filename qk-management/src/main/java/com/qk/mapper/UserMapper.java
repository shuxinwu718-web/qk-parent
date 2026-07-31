package com.qk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qk.dto.UserDto;
import com.qk.entity.User;  // 👈 导入 User
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户数据访问接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {  // 👈 改成 User
    /**
     * 动态条件分页查询用户列表
     * 如果 MyBatis-Plus 的 selectPage 能满足需求，这个方法可以删除
     */
    //   List<User> getUsers(UserDto userDto);


    /**
     * 根据用户名查询用户信息（关联查询角色标签）
     */
    @Select("SELECT u.*, r.label AS role_label FROM user u " +
            "LEFT JOIN role r ON u.role_id = r.id " +
            "WHERE u.username = #{username}")
    User selectByUsername(String username);
}