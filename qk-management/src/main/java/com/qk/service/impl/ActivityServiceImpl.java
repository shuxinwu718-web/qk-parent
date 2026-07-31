package com.qk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qk.common.PageResult;
import com.qk.entity.Activity;
import com.qk.exception.BusinessException;
import com.qk.mapper.ActivityMapper;
import com.qk.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    // 固定渠道来源枚举：1: 线上活动, 2: 推广介绍
    private static final Set<Integer> CHANNELS = new HashSet<>(Arrays.asList(1, 2));

    // 固定活动类型枚举：1: 课程折扣, 2: 代金券
    private static final Set<Integer> TYPES = new HashSet<>(Arrays.asList(1, 2));

    @Override
    public PageResult<Activity> listActivities(Integer channel, Integer type, Integer status, Integer page, Integer pageSize) {
        // 1. 构建分页参数
        Page<Activity> pageParam = new Page<>(page, pageSize);

        // 2. 构建查询条件
        LambdaQueryWrapper<Activity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(channel != null, Activity::getChannel, channel);
        wrapper.eq(type != null, Activity::getType, type);

        // 3. 活动状态为计算字段：由当前时间与开始/结束时间比较得出
        LocalDateTime now = LocalDateTime.now();
        if (status != null) {
            switch (status) {
                case 1: // 未开始：开始时间 > 当前时间
                    wrapper.gt(Activity::getStartTime, now);
                    break;
                case 2: // 进行中：开始时间 <= 当前时间 <= 结束时间
                    wrapper.le(Activity::getStartTime, now).ge(Activity::getEndTime, now);
                    break;
                case 3: // 已结束：结束时间 < 当前时间
                    wrapper.lt(Activity::getEndTime, now);
                    break;
                default:
                    throw new BusinessException("活动状态编码无效（1: 未开始, 2: 进行中, 3: 已结束）");
            }
        }
        wrapper.orderByDesc(Activity::getUpdateTime);

        // 4. 执行分页查询（逻辑删除已自动过滤）
        Page<Activity> pageResult = activityMapper.selectPage(pageParam, wrapper);

        // 5. 封装分页结果
        return new PageResult<>(pageResult.getTotal(), pageResult.getRecords());
    }

    @Override
    public Activity getActivityById(Integer id) {
        Activity activity = activityMapper.selectById(id);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }
        return activity;
    }

    @Override
    public void addActivity(Activity activity) {
        validateActivity(activity);
        activity.setCreateTime(LocalDateTime.now());
        activity.setUpdateTime(LocalDateTime.now());
        activity.setDeleted(0);
        activityMapper.insert(activity);
    }

    @Override
    public void updateActivity(Activity activity) {
        // 校验活动是否存在
        Activity existing = activityMapper.selectById(activity.getId());
        if (existing == null) {
            throw new BusinessException("活动不存在");
        }
        validateActivity(activity);
        activity.setUpdateTime(LocalDateTime.now());
        activityMapper.updateById(activity);
    }

    @Override
    public void deleteActivity(Integer id) {
        // 校验活动是否存在
        Activity existing = activityMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("活动不存在");
        }
        activityMapper.deleteById(id);
    }

    /**
     * 活动公共校验：枚举范围 + 类型联动（折扣/代金券二选一）+ 时间先后
     */
    private void validateActivity(Activity activity) {
        // 校验渠道来源编码
        if (activity.getChannel() == null || !CHANNELS.contains(activity.getChannel())) {
            throw new BusinessException("渠道来源编码不在可选范围内");
        }

        // 校验活动类型编码
        if (activity.getType() == null || !TYPES.contains(activity.getType())) {
            throw new BusinessException("活动类型编码不在可选范围内");
        }

        // 类型联动：课程折扣必填折扣，代金券必填金额
        if (activity.getType() == 1 && activity.getDiscount() == null) {
            throw new BusinessException("活动类型为课程折扣时，课程折扣不能为空");
        }
        if (activity.getType() == 2 && activity.getVoucher() == null) {
            throw new BusinessException("活动类型为代金券时，代金券金额不能为空");
        }

        // 结束时间必须晚于开始时间
        if (activity.getStartTime() == null || activity.getEndTime() == null) {
            throw new BusinessException("开始时间和结束时间不能为空");
        }
        if (!activity.getEndTime().isAfter(activity.getStartTime())) {
            throw new BusinessException("结束时间必须晚于开始时间");
        }
    }
}
