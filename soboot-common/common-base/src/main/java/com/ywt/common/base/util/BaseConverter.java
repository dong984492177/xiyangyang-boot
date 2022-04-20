package com.ywt.common.base.util;

import org.springframework.beans.BeanUtils;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @Author: huangchaoyang
 * @Description: 对象转换工具类基类
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public class BaseConverter {
    /**
     * 把指定类型的源对象的列表转换为结果对象的列表
     *
     * @param source 源对象的列表
     * @param converter 对象转换函数
     * @param <S> 源对象类型
     * @param <R> 结果对象类型
     * @return 结果对象的列表
     */
    public static <S, R> List<R> convert(Collection<S> source, Function<S, R> converter) {
        if(source == null)
            return null;

        return source.stream().map(converter).collect(Collectors.toList());
    }

    /**
     * 把指定类型的源对象的列表转换为结果对象的列表
     *
     * @param source 源对象的列表
     * @param converter 对象转换函数
     * @param resultObjCreator 结果对象的创建函数，不能为 null
     * @param <S> 源对象类型
     * @param <R> 结果对象类型
     * @return 结果对象的列表
     */
    public static <S, R> List<R> convert(Collection<S> source, BiFunction<S, Supplier<R>, R> converter, Supplier<R> resultObjCreator) {
        if(source == null)
            return null;

        return source.stream().map(s -> converter.apply(s, resultObjCreator)).collect(Collectors.toList());
    }

    /**
     * 把指定类型的源对象的列表转换为结果对象的列表，使用 BaseConverter.convert 作为转换函数
     *
     * @param source 源对象的列表
     * @param resultObjCreator 结果对象的创建函数，不能为 null
     * @param <S> 源对象类型
     * @param <R> 结果对象类型
     * @return 结果对象的列表
     */
    public static <S, R> List<R> convert(Collection<S> source, Supplier<R> resultObjCreator) {
        if(source == null)
            return null;

        return source.stream().map(s -> convert(s, resultObjCreator)).collect(Collectors.toList());
    }

    /**
     * 把指定类型的源对象的转换为结果对象，使用 resultObjCreator 创建对象，使用 BeanUtils.copyProperties 复制对象属性。
     *
     * 此方法适用于源对象和目标对象的属性名称及类型都相同的情况。
     *
     * 如果源对象和目标对象的属性名称及类型存在部分相同的情况，可以先使用此方法转换相同属性后再专门设置不同的属性值。
     *
     * @param source 源对象
     * @param resultObjCreator 结果对象的创建函数，不能为 null
     * @param <S> 源对象类型
     * @param <R> 结果对象类型
     * @return 结果对象
     */
    public static <S, R> R convert(S source, Supplier<R> resultObjCreator) {
        if(source == null)
            return null;

        R dto = resultObjCreator.get();
        BeanUtils.copyProperties(source, dto);
        return dto;
    }


}
