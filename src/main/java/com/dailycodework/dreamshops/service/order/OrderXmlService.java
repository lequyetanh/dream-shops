package com.dailycodework.dreamshops.service.order;

import com.dailycodework.dreamshops.payload.dto.order.OrderInfo;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class OrderXmlService {
    private final XmlMapper xmlMapper;

    public OrderXmlService() {
        this.xmlMapper = new XmlMapper();
        this.xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public byte[] exportOrders(List<OrderInfo> orders) throws IOException {
        OrdersWrapper wrapper = new OrdersWrapper();
        wrapper.orders = orders;
        return xmlMapper.writeValueAsBytes(wrapper);
    }

    @Data
    @JacksonXmlRootElement(localName = "orders")
    private static class OrdersWrapper {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "order")
        private List<OrderInfo> orders;
    }
}
