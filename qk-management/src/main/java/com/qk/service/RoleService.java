package com.qk.service;

import com.qk.common.PageResult;
import com.qk.entity.Role;
import java.util.List;

public interface RoleService {

    /**
     * 分页查询角色列表
     */
    PageResult<Role> listRoles(String name, Integer page, Integer pageSize);

    /**
     * 查询所有角色
     */
    List<Role> listAllRoles();

    /**
     * 新增角色
     */
    void addRole(Role role);

    /**
     * 修改角色
     */
    void updateRole(Role role);

    /**
     * 删除角色
     */
    void deleteRole(Integer id);

    /**
     * 根据ID查询角色
     */
    Role getRoleById(Integer id);
}