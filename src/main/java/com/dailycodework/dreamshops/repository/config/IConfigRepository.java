package com.dailycodework.dreamshops.repository.config;

import com.dailycodework.dreamshops.entity.Config;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IConfigRepository extends CrudRepository<Config, Long> {
    @Query(value = "SELECT * FROM config WHERE company_id = :companyId AND code IN (:codes)", nativeQuery = true)
    List<Config> findAllByCompanyIdAndCodes(@Param("companyId") Long companyId, @Param("codes") List<String> codes);

    Optional<Config> findByCompanyIdAndCode(Long companyId, String code);
}
