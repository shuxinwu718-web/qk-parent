package com.qk.controller;

import com.qk.common.PageResult;
import com.qk.common.Result;
import com.qk.dto.UserDto;
import com.qk.entity.User;
import com.qk.exception.BusinessException;
import com.qk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 1. 条件分页查询用户列表
     */
    @GetMapping
    public Result listUsers(UserDto userDto) {
        log.info("条件分页查询用户: {}", userDto);
        PageResult<User> pageResult = userService.getUsers(userDto);
        return Result.success(pageResult);
    }

    /**
     * 2. 删除用户（支持批量）
     * 路径格式：/users/1 或 /users/10,20 或 /users/2,3,4
     */
    @DeleteMapping("/{ids}")
    public Result deleteUsers(@PathVariable String ids) {
        log.info("删除用户: {}", ids);
        if (ids == null || ids.trim().isEmpty()) {
            throw new BusinessException("用户ID不能为空");
        }

        // 解析 ids，支持逗号分隔
        List<Integer> idList = Arrays.stream(ids.split(","))
                .map(String::trim)
                .filter(id -> !id.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        if (idList.isEmpty()) {
            throw new BusinessException("用户ID不能为空");
        }

        userService.deleteUsers(idList);
        return Result.success("删除成功");
    }

    /**
     * 3. 添加用户
     */
    @PostMapping
    public Result addUser(@Valid @RequestBody User user) {
        log.info("新增用户: {}", user);
        userService.addUser(user);
        return Result.success("添加成功");
    }

    /**
     * 4. 根据ID查询用户
     */
    @GetMapping("/{id}")
    public Result getUserById(@PathVariable Integer id) {
        log.info("根据ID查询用户: {}", id);
        if (id == null || id <= 0) {
            throw new BusinessException("用户ID无效");
        }
        User user = userService.getUserById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return Result.success(user);
    }

    /**
     * 5. 修改用户
     */
    @PutMapping
    public Result updateUser(@Valid @RequestBody User user) {
        log.info("修改用户: {}", user);
        if (user.getId() == null || user.getId() <= 0) {
            throw new BusinessException("用户ID无效");
        }
        userService.updateUser(user);
        return Result.success("修改成功");
    }

    /**
     * 6. 查询所有用户
     */
    @GetMapping("/list")
    public Result listAllUsers() {
        log.info("查询所有用户");
        List<User> userList = userService.listAllUsers();
        return Result.success(userList);
    }

    /**
     * 7. 根据角色标识查询用户
     */
    @GetMapping("/role/{roleLabel}")
    public Result getUsersByRoleLabel(@PathVariable String roleLabel) {
        log.info("根据角色标识查询用户: {}", roleLabel);
        if (roleLabel == null || roleLabel.trim().isEmpty()) {
            throw new BusinessException("角色标识不能为空");
        }
        List<User> userList = userService.getUsersByRoleLabel(roleLabel);
        return Result.success(userList);
    }

    /**
     * 8. 根据部门ID查询用户
     */
    @GetMapping("/dept/{deptId}")
    public Result getUsersByDeptId(@PathVariable Integer deptId) {
        log.info("根据部门ID查询用户: {}", deptId);
        if (deptId == null || deptId <= 0) {
            throw new BusinessException("部门ID无效");
        }
        List<User> userList = userService.getUsersByDeptId(deptId);
        return Result.success(userList);
    }
}