package com.alu.itoken.service.redis.controller;

import com.alu.itoken.service.redis.service.DLockApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DLockController {
    @Autowired
    DLockApi dLockApi;

    /**
     * 获取锁
     *
     * @param lockKey     锁
     * @param uniqueValue 能够唯一标识请求的值，以此保证锁的加解锁是同一个客户端
     * @param expireTime  过期时间, 单位：milliseconds
     * @return
     */
    @RequestMapping(value = "lock" ,method = RequestMethod.POST)
    boolean lock(String lockKey, String uniqueValue, int expireTime){
        return dLockApi.lock(lockKey,uniqueValue,expireTime);
    }

    /**
     * 使用Lua脚本保证解锁的原子性
     *
     * @param lockKey     锁
     * @param uniqueValue 能够唯一标识请求的值，以此保证锁的加解锁是同一个客户端
     * @return
     */
    @RequestMapping(value = "unlock" ,method = RequestMethod.POST)
    boolean unlock(String lockKey, String uniqueValue){
        return  dLockApi.unlock(lockKey,uniqueValue);
    }
}
