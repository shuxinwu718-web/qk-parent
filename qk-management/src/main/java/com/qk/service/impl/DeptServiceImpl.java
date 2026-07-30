package com.qk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qk.common.PageResult;
import com.qk.entity.Dept;
import com.qk.mapper.DeptMapper;
import com.qk.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 部门服务实现类
 */
@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    /**
     * 新增部门
     */
    @Override
    public void addDept(Dept dept){
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.insert(dept);
    }

    /**
     * 条件分页查询部门
     */
    @Override
    public PageResult<Dept> listDepts(String name, Integer status, Integer page, Integer pageSize) {
        // 构建分页参数
        Page<Dept> pageParam = new Page<>(page, pageSize);

        // 构建条件查询
        LambdaQueryWrapper<Dept> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null && !name.isEmpty(), Dept::getDeptName, name);
        wrapper.eq(status != null, Dept::getStatus, status);
        wrapper.orderByDesc(Dept::getCreateTime);

        // 执行分页查询
        Page<Dept> pageResult = deptMapper.selectPage(pageParam, wrapper);

        // 封装分页结果
        return new PageResult<>(pageResult.getTotal(), pageResult.getRecords());
    }

    @Override
    public Dept getDeptById(Integer id) {
        return deptMapper.selectById(id);
    }

    @Override
    public void updateDept(Dept dept) {
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.updateById(dept);
    }


    public void deleteDept(Integer id) {
        deptMapper.deleteById(id);
    }

    /**
     * 👇 新增：查询所有部门
     */
    @Override
    public List<Dept> listAllDepts() {
        LambdaQueryWrapper<Dept> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Dept::getId);  // 按ID升序
        return deptMapper.selectList(wrapper);
    }


}