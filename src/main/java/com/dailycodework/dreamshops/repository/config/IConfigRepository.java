package com.dailycodework.dreamshops.repository.config;

import com.dailycodework.dreamshops.entity.Config;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IConfigRepository extends CrudRepository<Config, Long> {
    @Query(value = "select * from config where config.companyId = ?1 and config.code in code", nativeQuery = true)
    List<Config> findAllByCompanyId(Long companyId, List<String> code);
}
