package com.dailycodework.dreamshops.repository.takeLog;

import com.dailycodework.dreamshops.entity.TaskLog;
import org.springframework.data.repository.CrudRepository;

public interface ITaskLogRepository extends CrudRepository<TaskLog,Integer> {
}
