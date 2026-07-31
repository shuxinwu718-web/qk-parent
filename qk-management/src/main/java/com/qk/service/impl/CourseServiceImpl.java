package com.qk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qk.common.PageResult;
import com.qk.entity.Course;
import com.qk.exception.BusinessException;
import com.qk.mapper.CourseMapper;
import com.qk.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    // 固定学科枚举：1-7
    private static final Set<Integer> SUBJECTS = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));

    // 固定适用人群枚举：1-3
    private static final Set<Integer> TARGETS = new HashSet<>(Arrays.asList(1, 2, 3));

    @Override
    public PageResult<Course> listCourses(String name, Integer subject, Integer target, Integer page, Integer pageSize) {
        // 1. 构建分页参数
        Page<Course> pageParam = new Page<>(page, pageSize);

        // 2. 构建查询条件
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(name), Course::getName, name);
        wrapper.eq(subject != null, Course::getSubject, subject);
        wrapper.eq(target != null, Course::getTarget, target);
        wrapper.orderByDesc(Course::getUpdateTime);

        // 3. 执行分页查询（逻辑删除已自动过滤）
        Page<Course> pageResult = courseMapper.selectPage(pageParam, wrapper);

        // 4. 封装分页结果
        return new PageResult<>(pageResult.getTotal(), pageResult.getRecords());
    }

    @Override
    public Course getCourseById(Integer id) {
        Course course = courseMapper.selectById(id);
        if (course == null) {
            throw new BusinessException("课程不存在");
        }
        return course;
    }

    @Override
    public void addCourse(Course course) {
        // 校验学科编码（1-7）
        if (course.getSubject() == null || !SUBJECTS.contains(course.getSubject())) {
            throw new BusinessException("课程学科编码不在可选范围内");
        }

        // 校验适用人群编码（1-3）
        if (course.getTarget() == null || !TARGETS.contains(course.getTarget())) {
            throw new BusinessException("适用人群编码不在可选范围内");
        }

        // 检查课程名称是否已存在（同名校验）
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Course::getName, course.getName());
        if (courseMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("课程名称已存在");
        }

        course.setCreateTime(LocalDateTime.now());
        course.setUpdateTime(LocalDateTime.now());
        course.setDeleted(0);
        courseMapper.insert(course);
    }

    @Override
    public void updateCourse(Course course) {
        // 校验课程是否存在
        Course existing = courseMapper.selectById(course.getId());
        if (existing == null) {
            throw new BusinessException("课程不存在");
        }

        // 校验学科编码（1-7）
        if (course.getSubject() == null || !SUBJECTS.contains(course.getSubject())) {
            throw new BusinessException("课程学科编码不在可选范围内");
        }

        // 校验适用人群编码（1-3）
        if (course.getTarget() == null || !TARGETS.contains(course.getTarget())) {
            throw new BusinessException("适用人群编码不在可选范围内");
        }

        // 检查课程名称是否已被其他课程使用
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Course::getName, course.getName())
                .ne(Course::getId, course.getId());
        if (courseMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("课程名称已存在");
        }

        course.setUpdateTime(LocalDateTime.now());
        courseMapper.updateById(course);
    }

    @Override
    public void deleteCourse(Integer id) {
        // 校验课程是否存在
        Course existing = courseMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("课程不存在");
        }

        courseMapper.deleteById(id);
    }
}