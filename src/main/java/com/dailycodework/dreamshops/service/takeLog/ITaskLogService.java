package com.dailycodework.dreamshops.service.takeLog;

import com.dailycodework.dreamshops.dto.BaseResultDTO;
import com.dailycodework.dreamshops.entity.TaskLog;
import org.springframework.data.domain.Pageable;

public interface ITaskLogService {
    public BaseResultDTO getTaskLogWithPaging(
            Pageable pageable,
            String keyword
    );
    public BaseResultDTO findById(Long id);
    public BaseResultDTO createTaskLog (TaskLog customerReq);
    public BaseResultDTO updateTaskLog (TaskLog customerReq);
    public BaseResultDTO deleteTaskLog (Long id);
}
