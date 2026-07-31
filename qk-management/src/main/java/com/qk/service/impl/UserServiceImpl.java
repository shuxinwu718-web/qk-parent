package com.qk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qk.common.PageResult;
import com.qk.dto.UserDto;
import com.qk.entity.Dept;
import com.qk.entity.LoginResultVo;
import com.qk.entity.Role;
import com.qk.entity.User;
import com.qk.exception.BusinessException;
import com.qk.mapper.DeptMapper;
import com.qk.mapper.RoleMapper;
import com.qk.mapper.UserMapper;
import com.qk.service.UserService;
import com.qk.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private RoleMapper roleMapper;


         /*
          登录
          */
    @Override
    public LoginResultVo login(String username, String password) {
        // 1. 根据用户名查询用户信息（包含角色标签）
        User user = userMapper.selectByUsername(username);

        // 2. 校验用户是否存在及密码是否正确
        if (user == null || !user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
            throw new BusinessException("用户名或密码错误");
        }

        // 3. 校验用户状态（0=停用）
        if (user.getStatus() == 0) {
            throw new BusinessException("对不起，您的账号已停用");
        }

        // 4. 构造登录结果
        LoginResultVo loginResult = new LoginResultVo();
        loginResult.setId(user.getId());
        loginResult.setUsername(user.getUsername());
        loginResult.setName(user.getName());
        loginResult.setImage(user.getImage());
        loginResult.setRoleLabel(user.getRoleLabel());

        // 5. 生成 JWT Token
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        claims.put("name", user.getName());
        claims.put("roleLabel", user.getRoleLabel());
        String token = JwtUtils.generateToken(claims);

        loginResult.setToken(token);

        return loginResult;
    }


    @Override
    public PageResult<User> getUsers(UserDto userDto) {
        Page<User> pageParam = new Page<>(userDto.getPage(), userDto.getPageSize());

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(userDto.getName()), User::getName, userDto.getName());
        wrapper.eq(userDto.getStatus() != null, User::getStatus, userDto.getStatus());
        wrapper.eq(userDto.getGender() != null, User::getGender, userDto.getGender());
        wrapper.eq(userDto.getDeptId() != null, User::getDeptId, userDto.getDeptId());
        wrapper.orderByDesc(User::getCreateTime);

        Page<User> pageResult = userMapper.selectPage(pageParam, wrapper);
        fillDeptAndRoleNames(pageResult.getRecords());

        return new PageResult<>(pageResult.getTotal(), pageResult.getRecords());
    }

    @Override
    public User getUserById(Integer id) {
        User user = userMapper.selectById(id);
        if (user != null) {
            fillDeptAndRoleNames(List.of(user));
        }
        return user;
    }

    @Override
    public void updateUser(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
    }

    @Override
    public void deleteUser(Integer id) {
        userMapper.deleteById(id);
    }

    @Override
    public void deleteUsers(List<Integer> ids) {
        if (ids != null && !ids.isEmpty()) {
            userMapper.deleteBatchIds(ids);
        }
    }

    @Override
    public void addUser(User user) {
        user.setPassword(DigestUtils.md5DigestAsHex((user.getPassword()+"123456").getBytes()));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);
    }

    @Override
    public List<User> listAllUsers() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(User::getId);
        List<User> userList = userMapper.selectList(wrapper);
        fillDeptAndRoleNames(userList);
        return userList;
    }

    @Override
    public List<User> getUsersByRoleLabel(String roleLabel) {
        // 1. 根据角色标识查询角色ID
        LambdaQueryWrapper<Role> roleWrapper = new LambdaQueryWrapper<>();
        roleWrapper.eq(Role::getLabel, roleLabel);
        Role role = roleMapper.selectOne(roleWrapper);
        if (role == null) {
            return List.of();  // 角色不存在，返回空列表
        }

        // 2. 根据角色ID查询用户
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getRoleId, role.getId());
        userWrapper.orderByDesc(User::getCreateTime);
        List<User> userList = userMapper.selectList(userWrapper);
        fillDeptAndRoleNames(userList);
        return userList;
    }

    @Override
    public List<User> getUsersByDeptId(Integer deptId) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getDeptId, deptId);
        wrapper.orderByDesc(User::getCreateTime);
        List<User> userList = userMapper.selectList(wrapper);
        fillDeptAndRoleNames(userList);
        return userList;
    }

    /**
     * 填充部门名称和角色名称
     */
    private void fillDeptAndRoleNames(List<User> userList) {
        if (userList == null || userList.isEmpty()) {
            return;
        }

        List<Integer> deptIds = userList.stream()
                .map(User::getDeptId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());

        List<Integer> roleIds = userList.stream()
                .map(User::getRoleId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());

        Map<Integer, String> deptMap = null;
        if (!deptIds.isEmpty()) {
            List<Dept> depts = deptMapper.selectBatchIds(deptIds);
            deptMap = depts.stream()
                    .collect(Collectors.toMap(Dept::getId, Dept::getDeptName));
        }

        Map<Integer, String> roleMap = null;
        if (!roleIds.isEmpty()) {
            List<Role> roles = roleMapper.selectBatchIds(roleIds);
            roleMap = roles.stream()
                    .collect(Collectors.toMap(Role::getId, Role::getName));
        }

        final Map<Integer, String> finalDeptMap = deptMap;
        final Map<Integer, String> finalRoleMap = roleMap;
        userList.forEach(user -> {
            if (user.getDeptId() != null && finalDeptMap != null) {
                user.setDeptName(finalDeptMap.get(user.getDeptId()));
            }
            if (user.getRoleId() != null && finalRoleMap != null) {
                user.setRoleName(finalRoleMap.get(user.getRoleId()));
            }
        });
    }
}