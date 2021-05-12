package com.alu.itoken.service.redis.service;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


@Service
public class RedisServiceImp implements RedisService {
	
	//命名要 redisTemplate 规范 要不报2个bean的错
	//StringRedisTemplate stringRedisTemplate;
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Override
	public String put(String key, Object value, long time,TimeUnit timeUnit) {

		redisTemplate.opsForValue().set(key, value, time,TimeUnit.SECONDS);
		return "service layer ok";
	}

	@Override
	public Object get(String key) {
		
		Object object=redisTemplate.opsForValue().get(key); 
		if (object!=null) {
			return String.valueOf(object );
		}
		return null;
	}

	@Override
	public String putB(String className, String id,String value) {
		// TODO Auto-generated method stub
		redisTemplate.boundHashOps(className).put(id, value);
		return "done";
	}

	@Override
	public String getB(String className, String key) {
		// TODO Auto-generated method stub
		return redisTemplate.boundHashOps(className).values().toString();
	}


	/**
	 * 通过连接池对象可以获得对redis的连接
	 */
	@Autowired
	JedisPool jedisPool;

	@Override
	public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
		Jedis jedis = null;// redis连接

		try {
			jedis = jedisPool.getResource();
			// 生成真正的存储于redis中的key
			String realKey = prefix.getPrefix() + key;
			// 通过key获取存储于redis中的对象（这个对象是以json格式存储的，所以是字符串）
			String strValue = jedis.get(realKey);
			// 将json字符串转换为对应的对象
			T objValue = stringToBean(strValue, clazz);
			return objValue;
		} finally {
			// 归还redis连接到连接池
			returnToPool(jedis);
		}
	}

	@Override
	public <T> boolean set(KeyPrefix prefix, String key, T value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			// 将对象转换为json字符串
			String strValue = beanToString(value);

			if (strValue == null || strValue.length() <= 0)
				return false;

			// 生成实际存储于redis中的key
			String realKey = prefix.getPrefix() + key;
			// 获取key的过期时间
			int expires = prefix.expireSeconds();

			if (expires <= 0) {
				// 设置key的过期时间为redis默认值（由redis的缓存策略控制）
				jedis.set(realKey, strValue);
			} else {
				// 设置key的过期时间
				jedis.setex(realKey, expires, strValue);
			}
			return true;
		} finally {
			returnToPool(jedis);
		}
	}

	@Override
	public boolean exists(KeyPrefix keyPrefix, String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = keyPrefix.getPrefix() + key;
			return jedis.exists(realKey);
		} finally {
			returnToPool(jedis);
		}
	}

	@Override
	public long incr(KeyPrefix keyPrefix, String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = keyPrefix.getPrefix() + key;
			return jedis.incr(realKey);
		} finally {
			returnToPool(jedis);
		}
	}

	@Override
	public long decr(KeyPrefix keyPrefix, String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = keyPrefix.getPrefix() + key;
			return jedis.decr(realKey);
		} finally {
			returnToPool(jedis);
		}
	}

	@Override
	public boolean delete(KeyPrefix prefix, String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = prefix.getPrefix() + key;
			Long del = jedis.del(realKey);
			return del > 0;
		} finally {
			returnToPool(jedis);
		}
	}

	/**
	 * 将对象转换为对应的json字符串
	 *
	 * @param value 对象
	 * @param <T>   对象的类型
	 * @return 对象对应的json字符串
	 */
	public static <T> String beanToString(T value) {
		if (value == null)
			return null;

		Class<?> clazz = value.getClass();
		/*首先对基本类型处理*/
		if (clazz == int.class || clazz == Integer.class)
			return "" + value;
		else if (clazz == long.class || clazz == Long.class)
			return "" + value;
		else if (clazz == String.class)
			return (String) value;
			/*然后对Object类型的对象处理*/
		else
			return JSON.toJSONString(value);
	}

	/**
	 * 根据传入的class参数，将json字符串转换为对应类型的对象
	 *
	 * @param strValue json字符串
	 * @param clazz    类型
	 * @param <T>      类型参数
	 * @return json字符串对应的对象
	 */
	public static <T> T stringToBean(String strValue, Class<T> clazz) {

		if ((strValue == null) || (strValue.length() <= 0) || (clazz == null))
			return null;

		// int or Integer
		if ((clazz == int.class) || (clazz == Integer.class))
			return (T) Integer.valueOf(strValue);
			// long or Long
		else if ((clazz == long.class) || (clazz == Long.class))
			return (T) Long.valueOf(strValue);
			// String
		else if (clazz == String.class)
			return (T) strValue;
			// 对象类型
		else
			return JSON.toJavaObject(JSON.parseObject(strValue), clazz);
	}

	/**
	 * 将redis连接对象归还到redis连接池
	 *
	 * @param jedis
	 */
	private void returnToPool(Jedis jedis) {
		if (jedis != null)
			jedis.close();
	}


}
