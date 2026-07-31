package com.qk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qk.common.PageResult;
import com.qk.dto.ClueQueryDto;
import com.qk.entity.Clue;

public interface ClueService extends IService<Clue> {
    /**
     * 线索列表
     */
    PageResult<Clue> listClues(ClueQueryDto clueQueryDto);
}
