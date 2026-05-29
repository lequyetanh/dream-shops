package com.dailycodework.dreamshops.repository.category;

import com.dailycodework.dreamshops.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE (:companyId IS NULL OR c.companyId = :companyId) AND (:keyword IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Category> findWithPaging(@Param("companyId") Long companyId, @Param("keyword") String keyword, Pageable pageable);
}
