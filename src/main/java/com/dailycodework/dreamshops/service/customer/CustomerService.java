package com.dailycodework.dreamshops.service.customer;

import com.dailycodework.dreamshops.constant.ResultNotify;
import com.dailycodework.dreamshops.entity.Customer;
import com.dailycodework.dreamshops.entity.Order;
import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.customer.CustomerInfo;
import com.dailycodework.dreamshops.payload.dto.customer.CustomerSpendingDTO;
import com.dailycodework.dreamshops.repository.customer.ICustomerRepository;
import com.dailycodework.dreamshops.repository.order.IOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService implements ICustomerService {
    private final ICustomerRepository customerRepository;
    private final IOrderRepository orderRepository;

    @Override
    public BaseResultDTO getCustomerWithPaging(Pageable pageable, String keyword, Long companyId) {
        Page<CustomerInfo> page = customerRepository.getCustomerWithPaging(pageable, keyword, companyId);
        return new BaseResultDTO(
                ResultNotify.successGet,
                true,
                page.getContent(),
                (int) page.getTotalElements()
        );
    }

    @Override
    public BaseResultDTO findById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.map(value -> new BaseResultDTO(ResultNotify.successGet, true, value))
                .orElseGet(() -> new BaseResultDTO(ResultNotify.notFound, false, null));
    }

    @Override
    public BaseResultDTO createCustomer(CustomerInfo customerReq) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerReq, customer);
        customerRepository.save(customer);
        return new BaseResultDTO(ResultNotify.successCreate, true, customer);
    }

    @Override
    public BaseResultDTO updateCustomer(CustomerInfo customerReq) {
        Optional<Customer> existingOpt = customerRepository.findById(customerReq.getId());
        if (existingOpt.isEmpty()) {
            return new BaseResultDTO(ResultNotify.notFound, false, null);
        }
        Customer customer = existingOpt.get();
        BeanUtils.copyProperties(customerReq, customer);
        customerRepository.save(customer);
        return new BaseResultDTO(ResultNotify.successUpdate, true, customer);
    }

    @Override
    public BaseResultDTO deleteCustomer(Long id) {
        customerRepository.deleteById(id);
        return new BaseResultDTO(ResultNotify.successDelete, true, null);
    }

    @Override
    public BaseResultDTO getOrderHistory(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerIdOrderByOrderDateDesc(customerId);
        return new BaseResultDTO(ResultNotify.successGet, true, orders, orders.size());
    }

    @Override
    public BaseResultDTO getSpendingSummary(Long customerId) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isEmpty()) {
            return new BaseResultDTO(ResultNotify.notFound, false, null);
        }
        Customer customer = customerOpt.get();
        List<Order> orders = orderRepository.findByCustomerIdOrderByOrderDateDesc(customerId);

        BigDecimal totalSpending = orders.stream()
                .filter(o -> o.getTotalAmount() != null)
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CustomerSpendingDTO summary = new CustomerSpendingDTO(
                customerId,
                customer.getName(),
                orders.size(),
                totalSpending
        );
        return new BaseResultDTO(ResultNotify.successGet, true, summary);
    }
}
