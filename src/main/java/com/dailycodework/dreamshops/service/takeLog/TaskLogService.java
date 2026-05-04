package com.dailycodework.dreamshops.service.takeLog;

import com.dailycodework.dreamshops.dto.BaseResultDTO;
import com.dailycodework.dreamshops.entity.TaskLog;
import com.dailycodework.dreamshops.repository.takeLog.ITaskLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskLogService implements ITaskLogService{
    private final ITaskLogRepository taskLogRepository;

    @Override
    public BaseResultDTO getTaskLogWithPaging(
            Pageable pageable,
            String keyword
    ){
        return null;
    };

    @Override
    public BaseResultDTO findById(Long id){
        return null;
    };

    @Override
    public BaseResultDTO createTaskLog (TaskLog customerReq){
        return null;
    };

    @Override
    public BaseResultDTO updateTaskLog (TaskLog customerReq){
        return null;
    };

    @Override
    public BaseResultDTO deleteTaskLog (Long id){
        return null;
    };
}
