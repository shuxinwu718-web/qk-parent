package com.qk.controller;

import com.qk.common.Result;
import com.qk.entity.Role;
import com.qk.exception.BusinessException;
import com.qk.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 角色管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 1. 分页查询角色列表
     */
    @GetMapping
    public Result listRoles(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("分页查询角色: name={}, page={}, pageSize={}", name, page, pageSize);
        return Result.success(roleService.listRoles(name, page, pageSize));
    }

    /**
     * 2. 查询所有角色列表（下拉框用）
     */
    @GetMapping("/list")
    public Result listAllRoles() {
        log.info("查询所有角色列表");
        return Result.success(roleService.listAllRoles());
    }

    /**
     * 3. 新增角色
     */
    @PostMapping
    public Result addRole(@Valid @RequestBody Role role) {
        log.info("新增角色: {}", role);
        roleService.addRole(role);
        return Result.success("添加成功");
    }

    /**
     * 4. 修改角色
     */
    @PutMapping
    public Result updateRole(@Valid @RequestBody Role role) {
        log.info("修改角色: {}", role);
        if (role.getId() == null) {
            throw new BusinessException("角色ID不能为空");
        }
        roleService.updateRole(role);
        return Result.success("修改成功");
    }

    /**
     * 5. 删除角色
     */
    @DeleteMapping("/{id}")
    public Result deleteRole(@PathVariable Integer id) {
        log.info("删除角色: {}", id);
        if (id == null || id <= 0) {
            throw new BusinessException("角色ID无效");
        }
        roleService.deleteRole(id);
        return Result.success("删除成功");
    }

    /**
     * 6.根据ID查询角色详情
     */
    @GetMapping("/{id}")
    public Result getRoleById(@PathVariable Integer id) {
        log.info("根据ID查询角色: {}", id);
        if (id == null || id <= 0) {
            throw new BusinessException("角色ID无效");
        }
        Role role = roleService.getRoleById(id);
        return Result.success(role);
    }

}