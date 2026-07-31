package com.qk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qk.dto.ClueQueryDto;
import com.qk.entity.Clue;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClueMapper extends BaseMapper<Clue> {
    /**
     * 线索列表
     */
    Page<Clue> listClues(Page<Clue> cluePage, ClueQueryDto clueQueryDto);
}