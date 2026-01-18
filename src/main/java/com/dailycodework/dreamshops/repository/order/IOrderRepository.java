package com.dailycodework.dreamshops.repository.order;

import com.dailycodework.dreamshops.entity.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderRepository extends CrudRepository<Order,Long>, OrderRepositoryCustom {
}
