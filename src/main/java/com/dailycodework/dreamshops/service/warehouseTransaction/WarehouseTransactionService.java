package com.dailycodework.dreamshops.service.warehouseTransaction;

import com.dailycodework.dreamshops.constant.ResultNotify;
import com.dailycodework.dreamshops.entity.Order;
import com.dailycodework.dreamshops.entity.OrderProduct;
import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.warehouseTransaction.WarehouseTransactionDetailReq;
import com.dailycodework.dreamshops.payload.dto.warehouseTransaction.WarehouseTransactionList;
import com.dailycodework.dreamshops.payload.dto.warehouseTransaction.WarehouseTransactionReq;
import com.dailycodework.dreamshops.entity.WarehouseTransaction;
import com.dailycodework.dreamshops.entity.WarehouseTransactionDetail;
import com.dailycodework.dreamshops.repository.warehouseTransaction.IWarehouseTransactionRepository;
import com.dailycodework.dreamshops.service.sequence.SequenceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class WarehouseTransactionService implements IWarehouseTransactionService {
    private final IWarehouseTransactionRepository warehouseTransactionRepository;
    private final SequenceService sequenceService;

    @Override
    public BaseResultDTO getWarehouseTransaction(
            Pageable pageable,
            String keyword,
            String fromDate,
            String toDate,
            Integer companyId
    ) {
        List<WarehouseTransactionList> warehouseTransactionResponse = new ArrayList<>();
        Page<WarehouseTransactionList> warehouseTransactionList = warehouseTransactionRepository.getWarehouseTransactionWithPaging(
                pageable,
                keyword,
                fromDate,
                toDate,
                companyId
        );
        warehouseTransactionResponse = warehouseTransactionList.getContent();
        return new BaseResultDTO(
                ResultNotify.successGet,
                true,
                warehouseTransactionResponse,
                (int) warehouseTransactionList.getTotalElements()
        );
    };

    @Override
    public BaseResultDTO findById(Long id){
        Optional<WarehouseTransaction> warehouseTransaction = warehouseTransactionRepository.findById(id);
        return warehouseTransaction.map(value -> new BaseResultDTO(
                ResultNotify.successGet,
                true,
                value
        )).orElseGet(() -> new BaseResultDTO(
                ResultNotify.notFound,
                false,
                null
        ));
    };
    @Override
        public BaseResultDTO createWarehouseTransaction(WarehouseTransactionReq warehouseTransactionReq){
        WarehouseTransaction warehouseTransaction = new WarehouseTransaction();
        List<WarehouseTransactionDetail> productList = new ArrayList<>();
        BeanUtils.copyProperties(warehouseTransactionReq,warehouseTransaction);
        for(WarehouseTransactionDetailReq prod : warehouseTransactionReq.getDetails()){
            WarehouseTransactionDetail warehouseTransactionDetail = new WarehouseTransactionDetail();
            BeanUtils.copyProperties(prod,warehouseTransactionDetail);
            warehouseTransactionDetail.setWarehouseTransaction(warehouseTransaction);
            productList.add(warehouseTransactionDetail);
        }
        warehouseTransaction.setWarehouseTransactionDetail(productList);
        warehouseTransactionRepository.save(warehouseTransaction);
        return new BaseResultDTO(
                ResultNotify.successCreate,
                true,
                warehouseTransaction
        );
    };
    @Override
    public BaseResultDTO updateWarehouseTransaction(WarehouseTransactionReq warehouseTransactionReq){
        return null;
    };
    @Override
    public BaseResultDTO deleteWarehouseTransaction(Long id){
        warehouseTransactionRepository.deleteById(id);
        return new BaseResultDTO(
                ResultNotify.successDelete,
                true,
                null
        );
    };

    public BaseResultDTO createWarehouseTransactionFromListOrder(List<Order> orders){
        for(Order order: orders){
            WarehouseTransaction warehouseTransaction = new WarehouseTransaction();
            warehouseTransaction.setCompanyId(order.getCompanyId());
            warehouseTransaction.setNo(sequenceService.getSequenceCode(order.getCompanyId(), "XK"));
            warehouseTransaction.setDate(ZonedDateTime.now());
            warehouseTransaction.setDescription(order.getDescription());
            List<WarehouseTransactionDetail> productList = new ArrayList<>();
            for(OrderProduct orderProduct : order.getProducts()){
                WarehouseTransactionDetail warehouseTransactionDetail = new WarehouseTransactionDetail();
                warehouseTransactionDetail.setProductId(orderProduct.getProductId());
                warehouseTransactionDetail.setQuantity(orderProduct.getQuantity());
                warehouseTransactionDetail.setWarehouseTransaction(warehouseTransaction);
                productList.add(warehouseTransactionDetail);
            }
            warehouseTransaction.setWarehouseTransactionDetail(productList);
            warehouseTransactionRepository.save(warehouseTransaction);
        }
        return null;
    }
}
