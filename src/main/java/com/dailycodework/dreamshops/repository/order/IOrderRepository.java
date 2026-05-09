package com.dailycodework.dreamshops.repository.order;

import com.dailycodework.dreamshops.entity.Company;
import com.dailycodework.dreamshops.entity.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderRepository extends CrudRepository<Order,Long>, OrderRepositoryCustom {
    List<Order> findByIdIn(List<Long> ids);

}
