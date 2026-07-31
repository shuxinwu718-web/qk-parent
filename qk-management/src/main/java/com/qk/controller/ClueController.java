package com.qk.controller;

import com.qk.common.PageResult;
import com.qk.dto.ClueQueryDto;
import com.qk.entity.Clue;
import com.qk.service.ClueService;
import com.qk.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/clues")
public class ClueController {

    @Autowired
    private ClueService clueService;

    /**
     * 新增线索
     */
    @PostMapping
    public Result addClue(@RequestBody Clue clue) {
        log.info("新增线索: {}", clue);
        clue.setStatus(1);
        clue.setCreateTime(LocalDateTime.now());
        clue.setUpdateTime(LocalDateTime.now());

        clueService.save(clue);
        return Result.success();
    }



    /**
     * 列表查询
     */
    @GetMapping
    public Result listClues(ClueQueryDto clueQueryDto) {
        log.info("查询参数: {}", clueQueryDto);
        PageResult<Clue> pageResult = clueService.listClues(clueQueryDto);
        return Result.success(pageResult);
    }


    /**
     * 分配线索
     *
     * @param clueId 线索ID
     * @param userId 用户ID
     * @return Result
     */
    @PutMapping("/assign/{clueId}/{userId}")
    public Result assignClue(@PathVariable Integer clueId, @PathVariable Integer userId) {
        log.info("分配线索: 线索ID={}, 用户ID={}", clueId, userId);

        // 查询线索是否存在
        Clue clue = new Clue();
        clue.setId(clueId);
        clue.setStatus(2); // 待跟进
        clue.setUserId(userId); // 设置归属人ID
        clue.setUpdateTime(java.time.LocalDateTime.now()); // 更新时间

        // 保存更新后的线索信息
        clueService.updateById(clue);
        return Result.success();
    }
}