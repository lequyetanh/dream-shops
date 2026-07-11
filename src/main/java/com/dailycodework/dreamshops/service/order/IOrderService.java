package com.dailycodework.dreamshops.service.order;

import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.order.OrderInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IOrderService {
    public BaseResultDTO getOrderWithPaging(
            Pageable pageable,
            String keyword,
            String fromDate,
            String toDate,
            String OrderCode,
            Integer Status,
            Integer companyId
    );
    public BaseResultDTO findById(Long id);
    public BaseResultDTO createOrder(OrderInfo orderReq);
    public BaseResultDTO updateOrder(OrderInfo orderReq);
    public BaseResultDTO updateOrderStatus(Long id, Integer status);
    public BaseResultDTO deleteOrder(Long id);

    public BaseResultDTO importOrdersFromExcel(MultipartFile file) throws IOException;
    public byte[] exportOrdersToExcel(
            String keyword, String fromDate, String toDate, String orderCode, Integer status, Integer companyId
    ) throws IOException;
    public byte[] exportOrdersToPdf(
            String keyword, String fromDate, String toDate, String orderCode, Integer status, Integer companyId
    ) throws IOException;
}
