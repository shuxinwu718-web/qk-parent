package com.qk.controller;

import com.qk.common.PageResult;
import com.qk.common.Result;
import com.qk.entity.Dept;
import com.qk.exception.BusinessException;
import com.qk.service.DeptService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/depts")
@Validated
public class DeptController {

    @Autowired
    private DeptService deptService;

    @PostMapping
    public Result addDept(@Valid @RequestBody Dept dept) {
        log.info("新增部门: {}", dept);
        deptService.addDept(dept);
        return Result.success("添加成功");
    }

    @GetMapping
    public Result listDepts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("条件分页查询部门: name={}, status={}, page={}, pageSize={}", name, status, page, pageSize);
        PageResult<Dept> pageResult = deptService.listDepts(name, status, page, pageSize);
        return Result.success(pageResult);
    }

    @GetMapping("/select/{id}")
    public Result getDeptById(@PathVariable Integer id) {
        log.info("根据ID查询部门: {}", id);
        if (id == null || id <= 0) {
            throw new BusinessException(400, "部门ID无效");
        }
        Dept dept = deptService.getDeptById(id);
        if (dept == null) {
            throw new BusinessException(404, "部门不存在");
        }
        return Result.success(dept);
    }

    @PutMapping
    public Result updateDept(@Valid @RequestBody Dept dept) {
        log.info("修改部门: {}", dept);
        if (dept.getId() == null || dept.getId() <= 0) {
            throw new BusinessException(400, "部门ID无效");
        }
        deptService.updateDept(dept);
        return Result.success("修改成功");
    }

    @DeleteMapping("/{id}")
    public Result deleteDept(@PathVariable("id") Integer id) {
        log.info("删除部门: {}", id);
        if (id == null || id <= 0) {
            throw new BusinessException(400, "部门ID无效");
        }
        deptService.deleteDept(id);
        return Result.success("删除成功");
    }

    /**
     * 查询所有部门列表
     */
    @GetMapping("/list")  // 👈 确保是 GET 请求
    public Result listAllDepts() {
        log.info("查询所有部门列表");
        List<Dept> deptList = deptService.listAllDepts();
        return Result.success(deptList);
    }
}