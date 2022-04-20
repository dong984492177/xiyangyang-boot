package com.ywt.message.service;

import com.ywt.common.service.BaseApiService;
import com.ywt.message.bean.MessageVendor;

public interface MessageVendorApiService extends BaseApiService {

    // 根据VendorId查询MessageVendor
    MessageVendor getById(Integer id);
}
