-- 活动信息表（渠道来源/活动类型为整数编码，对应前端契约 openapi）
-- channel: 1-线上活动 2-推广介绍
-- type:    1-课程折扣 2-代金券
-- 活动状态为计算字段（不落库）：1-未开始 2-进行中 3-已结束，由当前时间与开始/结束时间比较得出
create table activity
(
    id          int unsigned primary key auto_increment comment '活动ID，主键',
    channel     int          not null comment '渠道来源编码：1-线上活动，2-推广介绍',
    name        varchar(20)  not null comment '活动名称，1-20字',
    type        int          not null comment '活动类型编码：1-课程折扣，2-代金券',
    discount    decimal(3,1) null comment '课程折扣，一位整数+一位小数，如8.8（type=1时必填）',
    voucher     int unsigned null comment '代金券金额（元），整数1-5位，如500（type=2时必填）',
    description varchar(100) not null comment '活动简介，5-100字',
    start_time  datetime     not null comment '开始时间',
    end_time    datetime     not null comment '结束时间',
    create_time datetime     not null comment '创建时间',
    update_time datetime     not null comment '最后更新时间',
    deleted     tinyint unsigned not null default 0 comment '逻辑删除：0-未删除，1-已删除'
) comment '活动信息表';

INSERT INTO activity (id, channel, name, type, discount, voucher, description, start_time, end_time, create_time, update_time)
VALUES (1, 1, '新春大促活动0.8折', 1, 0.8, NULL, '新春课程大促销，先到先得', '2025-01-01 00:00:00', '2025-03-01 23:59:59', '2026-07-31 10:00:00', '2026-07-31 10:00:00'),
       (2, 1, '618 课程折扣狂欢', 1, 8.8, NULL, 'Java/Python热门课程限时 8.8 折', '2026-06-01 00:00:00', '2026-06-30 23:59:59', '2026-07-31 10:00:00', '2026-07-31 10:00:00'),
       (3, 2, '老带新送券活动', 2, NULL, 500, '老学员推荐新学员报名即送 500 元代金券', '2026-07-01 00:00:00', '2026-09-30 23:59:59', '2026-07-31 10:00:00', '2026-07-31 10:00:00'),
       (4, 2, '国庆限时送券活动', 2, NULL, 100, '国庆期间报名课程即送 100 元代金券', '2026-09-25 00:00:00', '2026-10-10 23:59:59', '2026-07-31 10:00:00', '2026-07-31 10:00:00');
