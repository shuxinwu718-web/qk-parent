package com.qk.service;

import com.qk.common.PageResult;
import com.qk.dto.UserDto;
import com.qk.entity.LoginResultVo;
import com.qk.entity.User;
import java.util.List;

public interface UserService {

    /**
     * 条件分页查询用户列表
     */
    PageResult<User> getUsers(UserDto userDto);

    /**
     * 根据ID查询用户
     */
    User getUserById(Integer id);

    /**
     * 修改用户
     */
    void updateUser(User user);

    /**
     * 删除单个用户
     */
    void deleteUser(Integer id);

    /**
     * 批量删除用户
     */
    void deleteUsers(List<Integer> ids);

    /**
     * 新增用户
     */
    void addUser(User user);

    /**
     * 查询所有用户
     */
    List<User> listAllUsers();

    /**
     * 根据角色标识查询用户
     */
    List<User> getUsersByRoleLabel(String roleLabel);

    /**
     * 根据部门ID查询用户
     */
    List<User> getUsersByDeptId(Integer deptId);

    /**
     * 👇 新增：用户登录
     */
    LoginResultVo login(String username, String password);
}