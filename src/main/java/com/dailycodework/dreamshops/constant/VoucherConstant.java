package com.dailycodework.dreamshops.constant;

public interface VoucherConstant {
    interface DiscountType {
        Integer PERCENT = 1;
        Integer AMOUNT = 2;
    }

    interface Status {
        Integer INACTIVE = 0;
        Integer ACTIVE = 1;
    }

    interface Message {
        String NOT_FOUND = "Không tìm thấy voucher";
        String INACTIVE = "Voucher đang bị vô hiệu hóa";
        String NOT_STARTED = "Voucher chưa đến thời gian áp dụng";
        String EXPIRED = "Voucher đã hết hạn";
        String USAGE_LIMIT_REACHED = "Voucher đã hết lượt sử dụng";
        String MIN_ORDER_AMOUNT_NOT_MET = "Đơn hàng chưa đạt giá trị tối thiểu để áp dụng voucher";
    }
}
