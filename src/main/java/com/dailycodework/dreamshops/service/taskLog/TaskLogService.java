package com.dailycodework.dreamshops.service.taskLog;

import com.dailycodework.dreamshops.constant.ResultNotify;
import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.entity.TaskLog;
import com.dailycodework.dreamshops.rabbitmq.producer.OrderProducer;
import com.dailycodework.dreamshops.repository.taskLog.ITaskLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskLogService implements ITaskLogService{
    private final ITaskLogRepository taskLogRepository;
    private final OrderProducer orderProducer;

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
    public BaseResultDTO createTaskLog (TaskLog taskLog){
        taskLogRepository.save(taskLog);
        return new BaseResultDTO(
                ResultNotify.successCreate,
                true,
                taskLog
        );
    };

    @Override
    public BaseResultDTO updateTaskLog (TaskLog taskLog){
        return null;
    };

    @Override
    public BaseResultDTO deleteTaskLog (Long id){
        return null;
    };
}
