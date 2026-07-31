package com.qk.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 线索跟进记录实体类
 */
@Data
public class ClueTrackRecord {
    private Integer id; // 跟进记录id, 主键
    private Integer clueId; // 线索id，关联线索id
    private Integer userId; // 跟进人id，关联用户id
    private Integer subject; // 意向学科，1:ai智能应用开发(java), 2:ai大模型开发(python)，3:ai鸿蒙开发，4:ai大数据，5:ai嵌入式，6:ai测试，7:ai运维
    private Integer level; // 意向等级, 1:近期学习、2:打算学习(考虑中)、3:进行了解、4:打酱油
    private String record; // 跟进记录
    private LocalDateTime nextTime; // 下次跟进时间
    private Integer type; // 跟进类型, 1:正常跟进、0:伪线索
    private Integer falseReason; // 伪线索原因, 1:空号、2:停机、3:竞品、4:无法联系、5:其他
    private LocalDateTime createTime; // 创建时间
}