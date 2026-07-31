package com.qk.controller;

import com.qk.common.PageResult;
import com.qk.common.Result;
import com.qk.entity.Activity;
import com.qk.exception.BusinessException;
import com.qk.service.ActivityService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    /**
     * 1. 分页条件查询活动列表
     * channel: 渠道来源编码(1: 线上活动, 2: 推广介绍)
     * type: 活动类型编码(1: 课程折扣, 2: 代金券)
     * status: 活动状态(1: 未开始, 2: 进行中, 3: 已结束)
     */
    @GetMapping
    public Result listActivities(
            @RequestParam(required = false) Integer channel,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("分页查询活动: channel={}, type={}, status={}, page={}, pageSize={}",
                channel, type, status, page, pageSize);
        PageResult<Activity> pageResult = activityService.listActivities(channel, type, status, page, pageSize);
        return Result.success(pageResult);
    }

    /**
     * 2. 根据ID查询活动详情
     */
    @GetMapping("/{id}")
    public Result getActivityById(@PathVariable Integer id) {
        log.info("根据ID查询活动: {}", id);
        if (id == null || id <= 0) {
            throw new BusinessException("活动ID无效");
        }
        Activity activity = activityService.getActivityById(id);
        return Result.success(activity);
    }

    /**
     * 3. 新增活动
     */
    @PostMapping
    public Result addActivity(@Valid @RequestBody Activity activity) {
        log.info("新增活动: {}", activity);
        activityService.addActivity(activity);
        return Result.success("创建成功");
    }

    /**
     * 4. 修改活动
     */
    @PutMapping
    public Result updateActivity(@Valid @RequestBody Activity activity) {
        log.info("修改活动: {}", activity);
        if (activity.getId() == null || activity.getId() <= 0) {
            throw new BusinessException("活动ID无效");
        }
        activityService.updateActivity(activity);
        return Result.success("修改成功");
    }

    /**
     * 5. 删除活动（逻辑删除）
     */
    @DeleteMapping("/{id}")
    public Result deleteActivity(@PathVariable Integer id) {
        log.info("删除活动: {}", id);
        if (id == null || id <= 0) {
            throw new BusinessException("活动ID无效");
        }
        activityService.deleteActivity(id);
        return Result.success("删除成功");
    }
}
