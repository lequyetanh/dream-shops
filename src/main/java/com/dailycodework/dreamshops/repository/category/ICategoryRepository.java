package com.dailycodework.dreamshops.repository.category;

import com.dailycodework.dreamshops.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "select c.name from category c where c.id = ?1", nativeQuery = true)
    String getNameById(Long id);
}
