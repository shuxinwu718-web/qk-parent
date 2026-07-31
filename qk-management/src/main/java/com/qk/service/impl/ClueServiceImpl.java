package com.qk.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qk.common.PageResult;
import com.qk.dto.ClueQueryDto;
import com.qk.entity.Clue;
import com.qk.mapper.ClueMapper;
import com.qk.service.ClueService;
import org.springframework.stereotype.Service;

@Service
public class ClueServiceImpl extends ServiceImpl<ClueMapper, Clue> implements ClueService {
    @Override
    public PageResult<Clue> listClues(ClueQueryDto clueQueryDto) {
        Page<Clue> page = this.baseMapper.listClues(new Page<Clue>(clueQueryDto.getPage(), clueQueryDto.getPageSize()), clueQueryDto);
        return new PageResult<Clue>(page.getTotal(), page.getRecords());
    }
}