package com.ywt.console.utils;


import java.util.List;

import org.springframework.util.CollectionUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;

/**
 * @Author: huangchaoyang
 * @Description: 生成串工具类
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public class GeneratorUtils {

        public static String generatorMac(Integer productId,Integer gatewayId,Integer roomId){

            List list = CollectionUtils.arrayToList(NumberUtil.generateRandomNumber(1, 9, 3));
            String mac =  CollectionUtil.join(list,"") + productId + gatewayId+ roomId;
            mac.substring(0,mac.length()<=20?mac.length():20);//mac 最大长度20

            return mac;
        }

}
