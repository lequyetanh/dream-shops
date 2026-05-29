package com.dailycodework.dreamshops.repository.order;

import com.dailycodework.dreamshops.entity.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderRepository extends CrudRepository<Order, Long>, OrderRepositoryCustom {
    List<Order> findByIdIn(List<Long> ids);
    List<Order> findByCustomerIdOrderByOrderDateDesc(Long customerId);
}
