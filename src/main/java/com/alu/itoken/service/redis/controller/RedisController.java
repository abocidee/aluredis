package com.alu.itoken.service.redis.controller;

import java.util.concurrent.TimeUnit;

import com.alu.itoken.service.redis.service.KeyPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alu.itoken.service.redis.service.RedisService;

@RestController
public class RedisController {
    
	
	@Autowired
	private RedisService redisService;
	
	@RequestMapping(value = "put",method = RequestMethod.POST)
	public String put(String key,String value,long time){
		redisService.put(key, value, time,TimeUnit.SECONDS);
		return "ok";
	}
	
	@RequestMapping(value = "boundHashPut",method = RequestMethod.POST)
	public String putB(String className,String key,String value) {
		
		redisService.putB(className,key, value);
		return "ok";
	}
	@RequestMapping(value = "get",method = RequestMethod.GET)
	public Object get(String key) {
	 return 	redisService.get(key);	
	}
	@RequestMapping(value = "boundHashGet",method = RequestMethod.GET)
	public Object getB(String className,String key) {
	 return 	redisService.getB(className,key);	
	}

	/**
	 * redis 的get操作，通过key获取存储在redis中的对象
	 *
	 * @param prefix key的前缀
	 * @param key    业务层传入的key
	 * @param clazz  存储在redis中的对象类型（redis中是以字符串存储的）
	 * @return 存储于redis中的对象
	 */
	@RequestMapping(value = "secGet",method = RequestMethod.GET)
	public  Object get(KeyPrefix prefix, String key, Class clazz){
		return redisService.get(prefix,key,clazz);
	}

	/**
	 * redis的set操作
	 *
	 * @param prefix 键的前缀
	 * @param key    键
	 * @param value  值
	 * @return 操作成功为true，否则为true
	 */
	@RequestMapping(value = "SecSet" ,method = RequestMethod.POST)
	public boolean set(KeyPrefix prefix, String key, Object value){
		return redisService.set(prefix,key,value);
	}

	/**
	 * 判断key是否存在于redis中
	 *
	 * @param keyPrefix key的前缀
	 * @param key
	 * @return
	 */
	@RequestMapping(value = "exist",method = RequestMethod.GET)
	public boolean exists(KeyPrefix keyPrefix, String key){
		return redisService.exists(keyPrefix,key);
	}

	/**
	 * 自增
	 *
	 * @param keyPrefix
	 * @param key
	 * @return
	 */
	@RequestMapping(value = "incr",method = RequestMethod.POST)
	public long incr(KeyPrefix keyPrefix, String key){
		return  redisService.incr(keyPrefix,key);
	}

	/**
	 * 自减
	 *
	 * @param keyPrefix
	 * @param key
	 * @return
	 */
	@RequestMapping(value = "decr",method = RequestMethod.POST)
	public long decr(KeyPrefix keyPrefix, String key){
		return redisService.decr(keyPrefix,key);
	}


	/**
	 * 删除缓存中的用户数据
	 *
	 * @param prefix
	 * @param key
	 * @return
	 */
	@RequestMapping(value = "delete",method = RequestMethod.POST)
	public boolean delete(KeyPrefix prefix, String key){
		return  redisService.delete(prefix,key);
	}
}
