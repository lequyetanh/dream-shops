package com.dailycodework.dreamshops.repository.customer;

import com.dailycodework.dreamshops.entity.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICustomerRepository extends CrudRepository<Customer,Long> {
}
