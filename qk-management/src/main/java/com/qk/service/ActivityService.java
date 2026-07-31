package com.qk.service;

import com.qk.common.PageResult;
import com.qk.entity.Activity;

public interface ActivityService {

    /**
     * 分页条件查询活动
     *
     * @param channel   渠道来源编码（1: 线上活动, 2: 推广介绍）
     * @param type      活动类型编码（1: 课程折扣, 2: 代金券）
     * @param status    活动状态（1: 未开始, 2: 进行中, 3: 已结束）
     */
    PageResult<Activity> listActivities(Integer channel, Integer type, Integer status, Integer page, Integer pageSize);

    /**
     * 根据ID查询活动
     */
    Activity getActivityById(Integer id);

    /**
     * 新增活动
     */
    void addActivity(Activity activity);

    /**
     * 修改活动
     */
    void updateActivity(Activity activity);

    /**
     * 删除活动（逻辑删除）
     */
    void deleteActivity(Integer id);
}
