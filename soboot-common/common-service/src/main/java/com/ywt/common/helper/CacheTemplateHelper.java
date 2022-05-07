package com.ywt.common.helper;

import com.alibaba.fastjson.JSON;
import com.ywt.common.base.util.BeanMapping;
import com.ywt.common.config.redis.RedissonService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @Author: huangchaoyang
 * @Description: 查询缓存模版方法类
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Component
public class CacheTemplateHelper<R> {

    @Autowired
    private RedissonService redissonService;

    public List<R> queryList(String key, String lockKey, Supplier<List<R>> queryList, Class<R> clazz){
        //查询缓存
        String json = redissonService.getString(key);
        if(StringUtils.isNotBlank(json)){
            List<R> rList = JSON.parseArray(json, clazz);
            if(!CollectionUtils.isEmpty(rList)){
                List<R> modelList = new ArrayList<>();
                BeanMapping.map(rList,modelList, clazz);
                return modelList;
            }
        }

        RLock rLock = redissonService.getRLock(lockKey);
        rLock.tryLock();
        try {
            String jsons = redissonService.getString(key);
            if(StringUtils.isNoneBlank(jsons)){
                List<R> rList = JSON.parseArray(jsons, clazz);
                if(!CollectionUtils.isEmpty(rList)){
                    List<R> modelList = new ArrayList<>();
                    BeanMapping.map(rList,modelList, clazz);
                    return modelList;
                }
            }

            List<R> list = queryList.get();
            if (!CollectionUtils.isEmpty(list)) {
                List<R> modelList = new ArrayList<>();
                BeanMapping.map(list, modelList, clazz);
                redissonService.setString(key, JSON.toJSONString(modelList));
            }
            return list;
        }finally{
            if (rLock.isLocked() && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
    }

    public <T> List<R> queryList(String key, String lockKey, Function<T, List<R>> queryList, T t, Class<R> clazz){
        //查询缓存
        String json = redissonService.getString(key);
        if(StringUtils.isNotBlank(json)){
            List<R> rList = JSON.parseArray(json, clazz);
            if(!CollectionUtils.isEmpty(rList)){
                List<R> modelList = new ArrayList<>();
                BeanMapping.map(rList,modelList, clazz);
                return modelList;
            }
        }

        RLock rLock = redissonService.getRLock(lockKey);
        rLock.tryLock();
        try {
            String jsons = redissonService.getString(key);
            if(StringUtils.isNoneBlank(jsons)){
                List<R> rList = JSON.parseArray(jsons, clazz);
                if(!CollectionUtils.isEmpty(rList)){
                    List<R> modelList = new ArrayList<>();
                    BeanMapping.map(rList,modelList, clazz);
                    return modelList;
                }
            }

            List<R> list = queryList.apply(t);
            if (!CollectionUtils.isEmpty(list)) {
                List<R> modelList = new ArrayList<>();
                BeanMapping.map(list, modelList, clazz);
                redissonService.setString(key, JSON.toJSONString(modelList));
            }
            return list;
        }finally{
            if (rLock.isLocked() && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
    }
}
