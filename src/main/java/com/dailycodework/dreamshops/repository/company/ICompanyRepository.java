package com.dailycodework.dreamshops.repository.company;

import com.dailycodework.dreamshops.entity.Company;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICompanyRepository extends CrudRepository<Company, Long> {
}
