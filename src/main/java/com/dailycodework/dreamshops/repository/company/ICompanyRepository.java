package com.dailycodework.dreamshops.repository.company;

import com.dailycodework.dreamshops.entity.Company;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICompanyRepository extends CrudRepository<Company, Long> {
    @Query(value = "select * from company c where c.id = ?1", nativeQuery = true)
    Optional<Company> findCompanyById(Long id);
}
