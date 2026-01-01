package com.dailycodework.dreamshops.service.customer;

import com.dailycodework.dreamshops.dto.BaseResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService implements ICustomerService{
    private final CustomerService customerService;

    @Override
    public BaseResultDTO getCustomerWithPaging(
            Pageable pageable,
            String keyword
    ){
        return null;
    };

    @Override
    public BaseResultDTO findById(Long id){
        return null;
    };

    @Override
    public BaseResultDTO createCustomer (){
        return null;
    };

    @Override
    public BaseResultDTO updateCustomer (){
        return null;
    };

    @Override
    public BaseResultDTO deleteCustomer (Long id){
        return null;
    };
}
