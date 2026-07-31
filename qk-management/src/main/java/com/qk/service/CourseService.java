package com.qk.service;

import com.qk.common.PageResult;
import com.qk.entity.Course;

public interface CourseService {

    /**
     * 分页条件查询课程
     */
    PageResult<Course> listCourses(String name, Integer subject, Integer target, Integer page, Integer pageSize);

    /**
     * 根据ID查询课程
     */
    Course getCourseById(Integer id);

    /**
     * 新增课程
     */
    void addCourse(Course course);

    /**
     * 修改课程
     */
    void updateCourse(Course course);

    /**
     * 删除课程（逻辑删除）
     */
    void deleteCourse(Integer id);
}