package com.qk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qk.common.PageResult;
import com.qk.entity.Role;
import com.qk.exception.BusinessException;
import com.qk.mapper.RoleMapper;
import com.qk.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public PageResult<Role> listRoles(String name, Integer page, Integer pageSize) {
        // 1. 构建分页参数
        Page<Role> pageParam = new Page<>(page, pageSize);

        // 2. 构建查询条件
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(name), Role::getName, name);
        wrapper.orderByDesc(Role::getCreateTime);

        // 3. 执行分页查询
        Page<Role> pageResult = roleMapper.selectPage(pageParam, wrapper);

        // 4. 封装分页结果
        return new PageResult<>(pageResult.getTotal(), pageResult.getRecords());
    }

    @Override
    public List<Role> listAllRoles() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Role::getId);
        return roleMapper.selectList(wrapper);
    }

    @Override
    public void addRole(Role role) {
        // 校验角色名称是否已存在
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getName, role.getName());
        if (roleMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("角色名称已存在");
        }

        // 校验角色标识是否已存在
        wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getLabel, role.getLabel());
        if (roleMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("角色标识已存在");
        }

        role.setCreateTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        roleMapper.insert(role);
    }

    @Override
    public void updateRole(Role role) {
        // 校验角色是否存在
        Role existing = roleMapper.selectById(role.getId());
        if (existing == null) {
            throw new BusinessException("角色不存在");
        }

        // 校验角色名称是否已被其他角色使用
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getName, role.getName())
                .ne(Role::getId, role.getId());
        if (roleMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("角色名称已存在");
        }

        // 校验角色标识是否已被其他角色使用
        wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getLabel, role.getLabel())
                .ne(Role::getId, role.getId());
        if (roleMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("角色标识已存在");
        }

        role.setUpdateTime(LocalDateTime.now());
        roleMapper.updateById(role);
    }

    @Override
    public void deleteRole(Integer id) {
        // 校验角色是否存在
        Role existing = roleMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("角色不存在");
        }

        // TODO: 检查是否有用户关联此角色，如果有则不能删除
        // 可以调用 UserMapper 查询是否有用户的 roleId 等于此 id

        roleMapper.deleteById(id);
    }

    @Override
    public Role getRoleById(Integer id) {
        Role role = roleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        return role;
    }
}