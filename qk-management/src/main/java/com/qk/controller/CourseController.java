package com.qk.controller;

import com.qk.common.PageResult;
import com.qk.common.Result;
import com.qk.entity.Course;
import com.qk.exception.BusinessException;
import com.qk.service.CourseService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    /**
     * 1. 分页条件查询课程列表
     */
    @GetMapping
    public Result listCourses(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer subject,
            @RequestParam(required = false) Integer target,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("分页查询课程: name={}, subject={}, target={}, page={}, pageSize={}",
                name, subject, target, page, pageSize);
        PageResult<Course> pageResult = courseService.listCourses(name, subject, target, page, pageSize);
        return Result.success(pageResult);
    }

    /**
     * 2. 根据ID查询课程详情
     */
    @GetMapping("/{id}")
    public Result getCourseById(@PathVariable Integer id) {
        log.info("根据ID查询课程: {}", id);
        if (id == null || id <= 0) {
            throw new BusinessException("课程ID无效");
        }
        Course course = courseService.getCourseById(id);
        return Result.success(course);
    }

    /**
     * 3. 新增课程
     */
    @PostMapping
    public Result addCourse(@Valid @RequestBody Course course) {
        log.info("新增课程: {}", course);
        courseService.addCourse(course);
        return Result.success("添加成功");
    }

    /**
     * 4. 修改课程
     */
    @PutMapping
    public Result updateCourse(@Valid @RequestBody Course course) {
        log.info("修改课程: {}", course);
        if (course.getId() == null || course.getId() <= 0) {
            throw new BusinessException("课程ID无效");
        }
        courseService.updateCourse(course);
        return Result.success("修改成功");
    }

    /**
     * 5. 删除课程（逻辑删除）
     */
    @DeleteMapping("/{id}")
    public Result deleteCourse(@PathVariable Integer id) {
        log.info("删除课程: {}", id);
        if (id == null || id <= 0) {
            throw new BusinessException("课程ID无效");
        }
        courseService.deleteCourse(id);
        return Result.success("删除成功");
    }
}