package com.dailycodework.dreamshops.constant;

public interface SystemLogConstant {
    interface Action {
        String ADD = "Thêm";
        String CREATE = "Tạo mới";
        String EDIT = "Cập nhật";
        String DELETE = "Xóa";
        String PUBLISH = "Phát hành";
        String IMPORT = "Import Excel";
        String GROUP = "Gộp đơn";
        String REPLACE = "Thay thế";
        String SEND_REQUEST = "Gọi món";
        String CALL_STAFF = "Nhân viên";
        String PAYMENT = "Thanh toán";
        String RESPONSE = "Phản hồi";
    }

    interface Type {
        String PRODUCT = "Product";
        String BILL = "Bill";
        String INVOICE = "Invoice";
        String CONFIG = "Config";
        String COMPANY = "Company";
        String GAS_LOG = "GasLog";
        String RS_WARD = "RS_WARD";
        String CUSTOMER = "Customer";
    }

    interface ActionType {
        String PUBLISH = "PUBLISH";
        String DELETE = "DELETE";
        String IMPORT = "IMPORT";
        String IMPORT_UPDATE = "IMPORT_UPDATE";
        String RS_IN_WARD = "RS_IN_WARD";
        String RS_OUT_WARD = "RS_OUT_WARD";
    }
}
