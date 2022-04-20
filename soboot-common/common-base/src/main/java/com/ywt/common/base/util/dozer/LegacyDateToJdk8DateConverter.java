package com.ywt.common.base.util.dozer;

import com.ywt.common.base.util.CoreDateUtils;
import org.dozer.CustomConverter;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public class LegacyDateToJdk8DateConverter implements CustomConverter {

    @Override
    public Object convert(Object destination, Object source, Class<?> destinationClass, Class<?> sourceClass) {

        if (destinationClass == null || sourceClass == null) {
            return destination;
        }

        if (source == null) {
            destination = null;
        }
        else if (destinationClass.isAssignableFrom(LocalDateTime.class) && sourceClass.isAssignableFrom(Date.class)) {
            destination = CoreDateUtils.convertFrom((Date) source);
        }
        else if (destinationClass.isAssignableFrom(Date.class) && sourceClass.isAssignableFrom(LocalDateTime.class)) {
            destination = CoreDateUtils.convertTo((LocalDateTime) source);
        }

        return destination;
    }
}
