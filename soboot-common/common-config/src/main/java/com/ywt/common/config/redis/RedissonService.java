package com.ywt.common.config.redis;

import org.redisson.api.*;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: huangchaoyang
 * @Description: Redisson 操作类
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Component
public class RedissonService {

    @Autowired(required = false)
    private RedissonClient redissonClient;

    /**`
     * 设置objectName和value
     * @return
     */
    public String getString(String objectName) {
        RBucket<String> bucket = redissonClient.getBucket(objectName);
        return bucket.get();
    }

    /**`
     * 设置objectName和value
     * @return
     */
    public String getStringWithStringCodec(String objectName) {
        RBucket<String> bucket = redissonClient.getBucket(objectName, new StringCodec());
        return bucket.get();
    }

    /**`
     * 设置objectName和value
     * @return
     */
    public void setString(String objectName, String value) {
    	RBucket<String> bucket = redissonClient.getBucket(objectName);
    	bucket.set(value);
    }

    /**`
     * 设置objectName和value
     * @return
     */
    public void setStringWithStringCodec(String objectName, String value) {
        RBucket<String> bucket = redissonClient.getBucket(objectName, new StringCodec());
        bucket.set(value);
    }


    /**`
     * 设置objectName和value,time失效时间
     * @return
     */
    public void setString(String objectName, String value, long time, TimeUnit timeUnit) {
        RBucket<String> bucket = redissonClient.getBucket(objectName);
        bucket.set(value,time,timeUnit);
    }

    /**
     * 删除objectName
     * @param objectName
     */
    public void deleteString(String objectName) {
        RBucket<String> bucket = redissonClient.getBucket(objectName);
        bucket.delete();
    }

    /**
     * 删除objectName
     * @param pattern
     */
    public void deleteByPattern(String pattern) {
        RKeys keys = redissonClient.getKeys();
        keys.deleteByPattern(pattern);
    }

    /**`
     * 获取字符串对象
     *
     * @param objectName
     * @return
     */
    public <T> RBucket<T> getRBucket(String objectName) {
        RBucket<T> bucket = redissonClient.getBucket(objectName);
        return bucket;
    }

    /**
     * 获取Map对象
     *
     * @param objectName
     * @return
     */
    public <K, V> RMap<K, V> getRMap(String objectName) {
        RMap<K, V> map = redissonClient.getMap(objectName);
        return map;
    }

    /**
     * 获取有序集合
     *
     * @param objectName
     * @return
     */
    public <V> RSortedSet<V> getRSortedSet(String objectName) {
        RSortedSet<V> sortedSet = redissonClient.getSortedSet(objectName);
        return sortedSet;
    }

    /**
     * 获取集合
     *
     * @param objectName
     * @return
     */
    public <V> RSet<V> getRSet(String objectName) {
        RSet<V> rSet = redissonClient.getSet(objectName);
        return rSet;
    }

    /**
     * 获取列表
     *
     * @param objectName
     * @return
     */
    public <V> RList<V> getRList(String objectName) {
        RList<V> rList = redissonClient.getList(objectName);
        return rList;
    }

    /**
     * 获取队列
     *
     * @param objectName
     * @return
     */
    public <V> RQueue<V> getRQueue(String objectName) {
        RQueue<V> rQueue = redissonClient.getQueue(objectName);
        return rQueue;
    }

    /**
     * 获取阻塞队列
     *
     * @param queueName
     * @return
     */
    public <V> RBlockingQueue<V> getRBlockingQueue(String queueName) {
        RBlockingQueue<V> rQueue = redissonClient.getBlockingQueue(queueName);
        return rQueue;
    }

    /**
     * 获取延时队列
     *
     * @param v        DTO传输类
     * @param delay    时间数量
     * @param timeUnit 时间单位
     * @param <V>      泛型
     * @return
     */
    public <V> void addQueue(V v, long delay, TimeUnit timeUnit, String queueName) {
        RBlockingQueue<V> blockingFairQueue = redissonClient.getBlockingQueue(queueName);
        RDelayedQueue<V> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
        delayedQueue.offer(v, delay, timeUnit);
        delayedQueue.destroy();
    }

    /**
     * 获取延时队列
     *
     * @param v        DTO传输类
     * @param delay    时间数量
     * @param timeUnit 时间单位
     * @param <V>      泛型
     * @return
     */
    public <V> void addQueue(V v, long delay, TimeUnit timeUnit, String queueName, List<ObjectListener> listenerList) {
        RBlockingQueue<V> blockingFairQueue = redissonClient.getBlockingQueue(queueName);
        listenerList.forEach(blockingFairQueue::addListener);
        RDelayedQueue<V> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
        delayedQueue.offer(v, delay, timeUnit);
        delayedQueue.destroy();
    }

    /**
     * 获取双端队列
     *
     * @param objectName
     * @return
     */
    public <V> RDeque<V> getRDeque(String objectName) {
        RDeque<V> rDeque = redissonClient.getDeque(objectName);
        return rDeque;
    }

    /**
     * 获取锁
     *
     * @param objectName
     * @return
     */
    public RLock getRLock(String objectName) {
        RLock rLock = redissonClient.getLock(objectName);
        return rLock;
    }

    /**
     * 获取读写锁
     *
     * @param objectName
     * @return
     */
    public RReadWriteLock getRWLock(String objectName) {
        RReadWriteLock rwlock = redissonClient.getReadWriteLock(objectName);
        return rwlock;
    }

    /**
     * 获取原子数
     *
     * @param objectName
     * @return
     */
    public RAtomicLong getRAtomicLong(String objectName) {
        RAtomicLong rAtomicLong = redissonClient.getAtomicLong(objectName);
        return rAtomicLong;
    }

    /**
     * 获取记数锁
     *
     * @param objectName
     * @return
     */
    public RCountDownLatch getRCountDownLatch(String objectName) {
        RCountDownLatch rCountDownLatch = redissonClient.getCountDownLatch(objectName);
        return rCountDownLatch;
    }

    /**
     * 获取消息的Topic
     *
     * @param objectName
     * @return
     */
    public RTopic getRTopic(String objectName) {
        RTopic rTopic = redissonClient.getTopic(objectName);
        return rTopic;
    }

    public RRateLimiter getRateLimiter(String limiterName) {
        RRateLimiter limiter = redissonClient.getRateLimiter(limiterName);
        return limiter;
    }

    /**
     * 获取信号量
     *
     * @param objectName
     * @return
     */
    public RSemaphore getSemaphore(String objectName) {
        RSemaphore semaphore = redissonClient.getSemaphore(objectName);
        return semaphore;
    }

    /**
     * 获取可过期的信号量
     *
     * @param objectName
     * @return
     */
    public RPermitExpirableSemaphore getPermitExpirableSemaphore(String objectName) {
        RPermitExpirableSemaphore semaphore = redissonClient.getPermitExpirableSemaphore(objectName);
        return semaphore;
    }

    /**
     * 获取长整型累加器
     *
     * @param objectName
     * @return
     */
    public RLongAdder getLongAdder(String objectName) {
        RLongAdder longAdder = redissonClient.getLongAdder(objectName);
        return longAdder;
    }
}
