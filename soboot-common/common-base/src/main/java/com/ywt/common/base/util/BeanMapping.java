package com.ywt.common.base.util;

import com.google.common.collect.Lists;
import com.ywt.common.base.exception.BeanMappingException;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;

/**
 * @Author: huangchaoyang
 * @Description: 不同类型的bean对象属性映射转化 基于Dozer封装, 对使用者透明
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public class BeanMapping {

    private final static Logger logger = LoggerFactory.getLogger(BeanMapping.class);

    private static Mapper __instance__;

    private static Lock lock = new ReentrantLock();

    private BeanMapping() {

    }

    private static Mapper getInstance() {
        if (__instance__ == null) {
            lock.lock();
            try {
                if (__instance__ == null) {
                    // 支持XML配置或者传入映射关系
                    List<String> mappingFiles = Lists.newArrayList();
                    mappingFiles.add("dozer/customDozerJdk8Converters.xml"); // 不支持设置多个configuration blocks

                    DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
                    dozerBeanMapper.setMappingFiles(mappingFiles);

                    __instance__ = dozerBeanMapper;
                }
            } finally {
                lock.unlock();
            }
        }
        return __instance__;
    }

    /**
     * 源对象 {@code source} 转换成目标bean对象 {@code D}; 将名称一致的属性进行转换
     * @param source 源Bean
     * @param dstClass 目标Bean的class
     * @param <S> 原Bean的泛型定义
     * @param <D> 目标Bean的泛型定义
     * @return 目标Bean对象
     */
    public static <S, D> D map(S source, Class<D> dstClass) throws BeanMappingException {
        return map(source, dstClass, null);
    }

    /**
     * 源对象 {@code source} 转换成目标bean对象 {@code D}; 将名称一致的属性进行转换; 转换完成后进行回调后续处理
     * @param source 源Bean
     * @param dstClass 目标Bean的class
     * @param biConsumer 目标Bean转换完成后的回调操作
     * @param <S> 原Bean的泛型定义
     * @param <D> 目标Bean的泛型定义
     * @return 目标Bean对象
     * @throws BeanMappingException
     */
    public static <S, D> D map(S source, Class<D> dstClass, BiConsumer<S, D> biConsumer) throws BeanMappingException {
        if (source == null) {
            return null;
        }
        try {
            D dstObject = getInstance().map(source, dstClass);
            if (dstObject != null && biConsumer != null) {
                biConsumer.accept(source, dstObject);
            }
            return dstObject;
        } catch (MappingException e) {
            logger.error("对象映射出错, 原对象类型: {}, 目标对象类型: {}", source.getClass(), dstClass);
            throw new BeanMappingException(e);
        }
    }

    /**
     * 源对象集合 {@code source} 转换成目标bean对象后添加到传入的集合中 {@code D}; 将名称一致的属性进行转换
     * @param source 源对象集合
     * @param destination 目标对象集合
     * @param dstClass 目标Bean的class
     * @param <S> 原Bean的泛型定义
     * @param <D> 目标Bean的泛型定义
     * @throws BeanMappingException
     */
    public static <S, D> void map(Collection<S> source, Collection<D> destination, Class<D> dstClass) throws BeanMappingException {
        map(source, destination, dstClass, null);
    }

    /**
     * 源对象集合 {@code source} 转换成目标bean对象后添加到传入的集合中 {@code D}; 将名称一致的属性进行转换
     * @param source 源对象集合
     * @param destination 目标对象集合
     * @param dstClass 目标Bean的class
     * @param biConsumer 目标Bean转换完成后的回调操作
     * @param <S> 原Bean的泛型定义
     * @param <D> 目标Bean的泛型定义
     * @throws BeanMappingException
     */
    public static <S, D> void map(Collection<S> source, Collection<D> destination, Class<D> dstClass, BiConsumer<S, D> biConsumer) throws BeanMappingException {
        for (S s : source) {
            destination.add(map(s, dstClass, biConsumer));
        }
    }

    /**
     * 源对象集合 {@code source} 转换成目标bean对象 {@code D}; 将名称一致的属性进行转换
     * @param source 源对象集合
     * @param dstClass 目标Bean的class
     * @param <S> 原Bean的泛型定义
     * @param <D> 目标Bean的泛型定义
     * @return 映射完的List集合, 默认返回ArrayList
     * @throws BeanMappingException
     */
    public static <S, D> List<D> mapList(Collection<S> source, Class<D> dstClass) throws BeanMappingException {
        return mapList(source, dstClass, null);
    }

    /**
     * 源对象集合 {@code source} 转换成目标bean对象 {@code D}; 将名称一致的属性进行转换
     * @param source 源对象集合
     * @param dstClass 目标Bean的class
     * @param biConsumer 目标Bean转换完成后的回调操作
     * @param <S> 原Bean的泛型定义
     * @param <D> 目标Bean的泛型定义
     * @return 映射完的List集合, 默认返回ArrayList
     * @throws BeanMappingException
     */
    public static <S, D> List<D> mapList(Collection<S> source, Class<D> dstClass, BiConsumer<S, D> biConsumer) throws BeanMappingException {
        List<D> result = new ArrayList<>();
        map(source, result, dstClass, biConsumer);
        return result;
    }


    // TODO 先实现最基本的映射, 碰到需求再完善

    /**
     *  源实体对象 {@code source} 转换成目标bean对象 {@code D}；
     *
     *  通过 {@code fieldMapping} 字段定义名称不同字段对应，key为源实体对象中字段名称，value为目标bean对象中字段名称。只需设置字段名称不相同字段。
     *
     *  通过 {@code excludingFieldSet} 字段定义需要排除的源实体对象 {@code source} 的字段
     *
     * @param source
     * @param destClass
     * @param fieldMapping
     * @param excludingFieldSet
     * @param <S>
     * @param <D>
     * @return
     * @throws Exception
     */
    /*
    public static <S, D> D convertEntityToBean(S source, Class<D> destClass, Map<String, String> fieldMapping, Set<String> excludingFieldSet) throws Exception {
        DozerBeanMapper mapper = new DozerBeanMapper();

        if ((fieldMapping != null && !fieldMapping.isEmpty()) || (excludingFieldSet != null && excludingFieldSet.size() > 0)) {
            final Map<String, String> internalFieldMapping = fieldMapping;
            BeanMappingBuilder builder = new BeanMappingBuilder() {
                @Override
                protected void configure() {
                    TypeMappingBuilder typeMappingBuilder = mapping(source.getClass(), destClass, TypeMappingOptions.oneWay());

                    if (fieldMapping != null && !fieldMapping.isEmpty()) {
                        for (String sourceField : internalFieldMapping.keySet()) {
                            String destField = internalFieldMapping.get(sourceField);
                            typeMappingBuilder.fields(sourceField, destField);
                        }
                    }

                    if (excludingFieldSet != null && excludingFieldSet.size() > 0) {
                        for (String excludingField : excludingFieldSet) {
                            typeMappingBuilder.exclude(excludingField);
                        }
                    }
                }
            };
            mapper.addMapping(builder);
        }

        return mapper.map(source, destClass);
    }
    */


    /**
     *  源实体对象 {@code source} 转换成目标bean对象 {@code D}；
     *
     *  通过 {@code fieldMapping} 字段定义名称不同字段对应，key为源实体对象中字段名称，value为目标bean对象中字段名称。只需设置字段名称不相同字段。
     *
     *  通过 {@code excludingFieldSet} 字段定义需要排除的源实体对象 {@code source} 的字段
     *
     * @param source
     * @param dest
     * @param fieldMapping
     * @param excludingFieldSet
     * @param <S>
     * @param <D>
     * @return
     * @throws Exception
     */
    /*
    public static <S, D> D convertEntityToBean(S source, D dest, Map<String, String> fieldMapping, Set<String> excludingFieldSet) throws Exception {
        DozerBeanMapper mapper = new DozerBeanMapper();

        if ((fieldMapping != null && !fieldMapping.isEmpty()) || (excludingFieldSet != null && excludingFieldSet.size() > 0)) {
            final Map<String, String> internalFieldMapping = fieldMapping;
            BeanMappingBuilder builder = new BeanMappingBuilder() {
                @Override
                protected void configure() {
                    TypeMappingBuilder typeMappingBuilder = mapping(source.getClass(), dest.getClass(), TypeMappingOptions.oneWay());

                    if (fieldMapping != null && !fieldMapping.isEmpty()) {
                        for (String sourceField : internalFieldMapping.keySet()) {
                            String destField = internalFieldMapping.get(sourceField);
                            typeMappingBuilder.fields(sourceField, destField);
                        }
                    }

                    if (excludingFieldSet != null && excludingFieldSet.size() > 0) {
                        for (String excludingField : excludingFieldSet) {
                            typeMappingBuilder.exclude(excludingField);
                        }
                    }
                }
            };
            mapper.addMapping(builder);
        }

        mapper.map(source, dest);
        return dest;
    }
    */
}
