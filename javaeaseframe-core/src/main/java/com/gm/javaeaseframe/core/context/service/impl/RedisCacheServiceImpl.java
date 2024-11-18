package com.gm.javaeaseframe.core.context.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.gm.javaeaseframe.core.context.service.ICacheService;

public class RedisCacheServiceImpl implements ICacheService {

    protected Log LOGGER = LogFactory.getLog(this.getClass());
    // 使用需要注入spring-data-redis的StringRedisTemplate
    @Autowired
    private StringRedisTemplate redisTemplate;
    /** key 前缀规则 */
    private String keyPreRule = null;

    @Override
	public boolean isShareCache()
	{
		return true;
	}



	@Override
    public boolean del(String... keys) {
        if (keys == null || keys.length == 0) {
            return true;
        }
        boolean ret = true;
        for (String key : keys) {
            if (!isValidKey(key)) {
                ret = false;
                break;
            }
            redisTemplate.delete(key);
        }
        return ret;
    }

    @Override
    public boolean isExists(String key) {
        boolean isExists = false;
        if (!isValidKey(key)) {
            return isExists;
        }
        return redisTemplate.hasKey(key);
    }

    @Override
    public boolean set(String key, Object value) {
        if (!isValidKey(key)) {
            return false;
        }
        if (value instanceof String) {
            redisTemplate.opsForValue().set(key, value.toString());
        } else {
            String objectJson = JSON.toJSONString(value);
            redisTemplate.opsForValue().set(key, objectJson);
        }
        return true;
    }
    
    public boolean set(String key, Object value, int secs) {
        return this.set(key, value, secs, TimeUnit.SECONDS);
    }
    
    @Override
	public boolean set(String key, Object value, long timeout, TimeUnit unit) {
    	if (!isValidKey(key)) {
            return false;
        }
        String objectJson = JSON.toJSONString(value);
        if (unit == null) {
			unit = TimeUnit.SECONDS;
		}
        redisTemplate.opsForValue().set(key, objectJson, timeout, unit);
		return true;
	}

    @Override
    public boolean setnx(String key, Object value) {
        if (!isValidKey(key)) {
            return false;
        }
        String objectJson = JSON.toJSONString(value);
        return redisTemplate.opsForValue().setIfAbsent(key, objectJson);
    }

    @Override
	public boolean setnx(String key, Object value, long timeout) {
		return this.setnx(key, value, timeout, TimeUnit.SECONDS);
	}

	@Override
	public boolean setnx(String key, Object value, long timeout, TimeUnit unit) {
		if (!isValidKey(key)) {
            return false;
        }
        String objectJson = JSON.toJSONString(value);
        if (unit == null) {
			unit = TimeUnit.SECONDS;
		}
		return redisTemplate.opsForValue().setIfAbsent(key, objectJson, timeout, unit);
	}

	@Override
	public boolean setex(String key, Object value) {
		if (!isValidKey(key)) {
            return false;
        }
        String objectJson = JSON.toJSONString(value);
		return redisTemplate.opsForValue().setIfPresent(key, objectJson);
	}

	@Override
	public boolean setex(String key, Object value, long timeout) {
		return this.setex(key, value, timeout, TimeUnit.SECONDS);
	}

	@Override
	public boolean setex(String key, Object value, long timeout, TimeUnit unit) {
		if (!isValidKey(key)) {
            return false;
        }
        String objectJson = JSON.toJSONString(value);
        if (unit == null) {
			unit = TimeUnit.SECONDS;
		}
		return redisTemplate.opsForValue().setIfPresent(key, objectJson, timeout, unit);
	}

	public boolean setBit(String key, long offset, boolean value)
    {
    	return redisTemplate.opsForValue().setBit(key, offset, value);
    }
    public boolean getBit(String key, long offset)
    {
    	return redisTemplate.opsForValue().getBit(key, offset);
    }
    private byte[] rawKey(String key)
    {
    	Assert.notNull(key, "non null key required");
    	org.springframework.data.redis.serializer.RedisSerializer<String> rs = (RedisSerializer<String>) redisTemplate.opsForValue().getOperations().getKeySerializer();
    	return rs.serialize(key);
    }
    public Long bitCount(String key)
    {
    	final byte[] rawKey = rawKey(key);
		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) {
				return connection.bitCount(rawKey);
			}
		}, true);
    }
    public Long bitCount(String key, final long start, final long end)
    {
    	final byte[] rawKey = rawKey(key);
    	return redisTemplate.execute(new RedisCallback<Long>() {
    		public Long doInRedis(RedisConnection connection) {
    			return connection.bitCount(rawKey, start, end);
    		}
    	}, true);
    }
    public long bitop(final BitOperation op, String destKey, String ... keys)
    {
    	if(op == null)
    	{
    		return 0;
    	}
    	if(keys == null || keys.length == 0)
    	{
    		return 0;
    	}
    	final byte[] destRawKey =  rawKey(destKey);
    	final List<byte[]> list = new ArrayList<byte[]>();
    	for(String key : keys)
    	{
    		if(StringUtils.isNotEmpty(key))
    		{
    			list.add(rawKey(key));
    		}
    	}
		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) {
				org.springframework.data.redis.connection.RedisStringCommands.BitOperation bop = null;
				switch(op)
				{
					case AND:
						bop = org.springframework.data.redis.connection.RedisStringCommands.BitOperation.AND;
						break;
					case OR:
						bop = org.springframework.data.redis.connection.RedisStringCommands.BitOperation.OR;
						break;
					case XOR:
						bop = org.springframework.data.redis.connection.RedisStringCommands.BitOperation.XOR;
						break;
					case NOT:
						bop = org.springframework.data.redis.connection.RedisStringCommands.BitOperation.NOT;
						break;
				}
				return connection.bitOp(bop, destRawKey, list.toArray(new byte[][]{}));
			}
		}, true);
    }
    
    @Override
	public boolean mset(Map<String, Object> map)
	{
		if(map == null || map.isEmpty())
		{
			return false;
		}
		Map<String, String> mapTmp = new HashMap<String, String>();
		for(Map.Entry<String, Object> mm : map.entrySet())
		{
			String key = mm.getKey();
			if (!isValidKey(key)) {
	            continue;
	        }
			String objectJson = JSON.toJSONString(mm.getValue());
			mapTmp.put(key, objectJson);
		}
		if(mapTmp == null || mapTmp.isEmpty())
		{
			return false;
		}
		redisTemplate.opsForValue().multiSet(mapTmp);
		mapTmp.clear();
		return true;
	}

	@Override
    public Long getLong(String key) {
        Long value = 0L;
        String str = (String) get(key);
        if (StringUtils.isNumeric(str)) {
            value = Long.parseLong(str);
        }
        return value;
    }

    /**
     * 根据key 获取对象
     *
     * @param key
     * @return
     */
    @Override
    public <T> T get(String key, Class<T> clazz) {
        if (!isValidKey(key)) {
            return null;
        }
        String value = redisTemplate.opsForValue().get(key);
        return convertObjs(value, clazz);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public String getSet(String key, String value) {
        return redisTemplate.opsForValue().getAndSet(key, value);
    }

    @Override
    public Long decrBy(String key, long value) {
        return redisTemplate.opsForValue().increment(key, -value);
    }

    @Override
    public Long decr(String key) {
        return redisTemplate.opsForValue().increment(key, -1);
    }

    @Override
    public Long incrBy(String key, long value) {
        return redisTemplate.opsForValue().increment(key, value);
    }

    @Override
    public Double incrByFloat(String key, double value) {
        return redisTemplate.opsForValue().increment(key, value);
    }

    @Override
    public Long incr(String key) {
        return redisTemplate.opsForValue().increment(key, 1);
    }

    @Override
	public Long incrForToday(String key)
	{
    	Long ret = redisTemplate.opsForValue().increment(key, 1);
    	if(ret != null && ret.longValue() == 1)
	   	{
    		Calendar cal = Calendar.getInstance();
    		cal.set(Calendar.HOUR_OF_DAY, 23);
    		cal.set(Calendar.MINUTE, 59);
    		cal.set(Calendar.SECOND, 59);
    		cal.set(Calendar.MILLISECOND, 999);
	   		 //添加有效期设置
    		redisTemplate.expireAt(key, cal.getTime());
	   	}
		return ret;
	}

	@Override
    public Long append(String key, String value) {
        return redisTemplate.opsForValue().append(key, value).longValue();
    }

    @Override
    public String substr(String key, int start, int end) {
        return redisTemplate.opsForValue().get(key).substring(start, end);
    }

    @Override
    public boolean hset(String key, String field, Object value) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        String objectJson = this.convertObjs(value);
        ops.put(key, field, objectJson);
        return true;
    }

    @SuppressWarnings("unchecked")
	@Override
    public <T> T hget(String key, String field) {
        return (T) redisTemplate.opsForHash().get(key, field);
    }

    @Override
    public <T> T hget(String key, String field, Class<T> clazz) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        return convertObjs(ops.get(key, field), clazz);
    }

    @Override
    public boolean hsetnx(String key, String field, Object value) {
        return redisTemplate.opsForHash().putIfAbsent(key, field, this.convertObjs(value));
    }

    @Override
    public boolean hmset(String key, Map<String, Object> hash) {
    	Map<String, String> map = new HashMap<String, String>();
    	for(Map.Entry<String, Object> entry : hash.entrySet())
    	{
    		map.put(entry.getKey(), this.convertObjs(entry.getValue()));
    	}
        redisTemplate.opsForHash().putAll(key, map);
        return true;
    }

    @Override
    public List<String> hmget(String key, String... fields) {
        Collection<String> hashKeys = Arrays.asList(fields);
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        return ops.multiGet(key, hashKeys);
    }

    @Override
	public <T> List<T> hmget(String key, Class<T> clazz, String... fields)
	{
    	List<String> list = this.hmget(key, fields);
    	if(list == null || list.isEmpty())
    	{
    		return new ArrayList<>();
    	}
    	List<T> retList = new ArrayList<>();
    	for(String value : list)
    	{
    		retList.add(convertObjs(value, clazz));
    	}
		return retList;
	}

	@Override
    public Long hincrBy(String key, String field, long value) {
        return redisTemplate.opsForHash().increment(key, field, value);
    }

    @Override
    public Double hincrByFloat(String key, String field, double value) {
        return redisTemplate.opsForHash().increment(key, field, value);
    }

	@Override
	public Long hincrByForTime(String key, String field, long value, long timeout)
	{
		boolean hasKey = redisTemplate.opsForHash().hasKey(key, field);
		Long ret = redisTemplate.opsForHash().increment(key, field, value);
		if(!hasKey)
	   	{
	   		 //添加有效期设置
    		redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
	   	}
		return ret;
	}

	@Override
    public boolean hexists(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    @Override
    public void hdel(String key, String ... field) {
        HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
        ops.delete(key, field);
    }

    @Override
    public Long hlen(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    @Override
    public Set<String> hkeys(String key) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        return ops.keys(key);
    }

    @Override
    public List<String> hvals(String key) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        return ops.values(key);
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        return ops.entries(key);
    }

    @Override
    public <T> Map<String, T> hgetAll(String key, Class<T> clazz) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        Map<String, String> map =  ops.entries(key);
        Map<String, T> retMap = new HashMap<String, T>();
        for(Map.Entry<String, String> entry : map.entrySet())
        {
        	retMap.put(entry.getKey(), convertObjs(entry.getValue(), clazz));
        }
        return retMap;
    }

    /**
     * 判断是否为有效Key,用于防止缓存穿透
     * @param key 缓存key
     * @return 是否有效
     */
    private boolean isValidKey(String key) {
        if (StringUtils.isEmpty(keyPreRule)) {
            return true;
        }
        boolean result = key.startsWith(keyPreRule);
        LOGGER.error("[Jedis]invalid key:" + key);
        return result;
    }

    public String getKeyPreRule() {
        return keyPreRule;
    }

    public void setKeyPreRule(String keyPreRule) {
        this.keyPreRule = keyPreRule;
    }
    
    public Long sadd(String key, Object ... values)
    {
    	 if(values == null || values.length == 0)
    	 {
    		 return 0L;
    	 }
    	 String[] strs = new String[values.length];
    	 for(int i = 0; i < values.length; i++)
    	 {
    		 Object value = values[i];
    		 if(value == null)
    		 {
    			 continue;
    		 }
    		 strs[i] = this.convertObjs(value);
    	 }
    	return redisTemplate.opsForSet().add(key, strs);
    }
    
	@Override
	public Long srem(String key, Object ... values)
	{
		if (values == null || values.length == 0)
		{
			return 0L;
		}
		Object[] strs = new Object[values.length];
		for (int i = 0; i < values.length; i++)
		{
			Object value = values[i];
			if (value == null)
			{
				continue;
			}
			strs[i] = this.convertObjs(value);
		}
		return redisTemplate.opsForSet().remove(key, strs);
	}
	
	@Override
	public String spop(String key)
	{
		return redisTemplate.opsForSet().pop(key);
	}

	@Override
	public <T> T spop(String key, Class<T> clazz)
	{
		String value = this.spop(key);
		return convertObjs(value, clazz);
	}

	@Override
	public Set<String> smembers(String key)
	{
		return redisTemplate.opsForSet().members(key);
	}
	@Override
	public boolean sismembers(String key, Object members)
	{
		return redisTemplate.opsForSet().isMember(key, members);
	}

	@Override
	public <T> Set<T> smembers(String key, Class<T> clazz)
	{
		Set<String> set = redisTemplate.opsForSet().members(key);
		Set<T> ret = new HashSet<T>();
		for(String value : set)
		{
			ret.add(convertObjs(value, clazz));
		}
		return ret;
	}
	
	@Override
	public List<String> srandmember(String key, Long count)
	{
		List<String> ret = new ArrayList<String>();
		if(count == null || count.longValue() == 0)
		{
			ret.add(redisTemplate.opsForSet().randomMember(key));
			return ret;
		}
		else if(count.longValue() < 0)
		{
			ret.addAll(redisTemplate.opsForSet().randomMembers(key, Math.abs(count.longValue())));
		}
		else
		{
			ret.addAll(redisTemplate.opsForSet().distinctRandomMembers(key, count));
		}
		return ret;
	}
	
	@Override
	public <T> List<T> srandmember(String key, Long count, Class<T> clazz)
	{
		List<T> ret = new ArrayList<T>();
		if(count == null || count.longValue() == 0)
		{
			String value = redisTemplate.opsForSet().randomMember(key);
			ret.add(convertObjs(value, clazz));
			return ret;
		}
		else if(count.longValue() < 0)
		{
			List<String> set = redisTemplate.opsForSet().randomMembers(key, Math.abs(count.longValue()));
			for(String value : set)
			{
				ret.add(convertObjs(value, clazz));
			}
		}
		else
		{
			Set<String> set = redisTemplate.opsForSet().distinctRandomMembers(key, count);
			for(String value : set)
			{
				ret.add(convertObjs(value, clazz));
			}
		}
		return ret;
	}

	@Override
	public Long lpush(String key, Object... values)
	{
		if (values == null || values.length == 0)
		{
			return 0L;
		}
		if (!isValidKey(key)) {
            return 0L;
        }
		return redisTemplate.opsForList().leftPushAll(key, this.convertObjs(values));
	}
	
	
	@Override
	public Long lpushForTime(String key, long timeout, Object... values)
	{
		Long ret = lpush(key, values);
		if(ret != null && ret.longValue() == values.length)
		{
			redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
		}
		return ret;
	}
	
	@Override
	public List<String> lrange(String key)
	{
		return redisTemplate.opsForList().range(key, 0, -1);
	}
	
	@Override
	public <T> List<T> lrange(String key, Class<T> clazz)
	{
		List<String> list = redisTemplate.opsForList().range(key, 0, -1);
		List<T> ret = new ArrayList<T>();
		for(String value : list)
		{
			ret.add(convertObjs(value, clazz));
		}
		return ret;
	}
	
	@Override
	public long lrem(String key, long count, Object value)
	{
		if (!isValidKey(key)) {
            return 0L;
        }
		return redisTemplate.opsForList().remove(key, count, value);
	}

	@Override
	public Long incrForTime(String key, long timeout)
	{
		Long ret = redisTemplate.opsForValue().increment(key, 1);
    	if(ret != null && ret.longValue() == 1)
	   	{
	   		 //添加有效期设置
    		redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
	   	}
		return ret;
	}

	@Override
	public boolean set(String key, Object value, long timeout)
	{
		if (!isValidKey(key)) {
            return false;
        }
        if (value instanceof String) {
            redisTemplate.opsForValue().set(key, value.toString());
        } else {
            String objectJson = JSON.toJSONString(value);
            redisTemplate.opsForValue().set(key, objectJson);
        }
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        return true;
	}
	
	@Override
	public boolean expire(String key, long timeout) {
		return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
	}

	@Override
	public boolean expireAt(String key, Date time) {
		if(time != null)
		{
			return redisTemplate.expireAt(key, time);
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public Long getExpire(String key) {
		return redisTemplate.getExpire(key);
	}



	/**
	 * Transactional 自动加入multi()/exec()
	 */
	@Override
	@Transactional
	public Long incrForTimeWithDelay(String key, long timeout) {
		Long ret = redisTemplate.opsForValue().increment(key, 1);
		redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
		return ret;
	}

	@Override
	@Transactional
	public Long saddForTimeWithDelay(String key, long timeout, Object... values) {
		Long ret = this.sadd(key, values);
		redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
		return ret;
	}
	
	@Override
	public Long rpush(String key, Object... values)
	{
		if(values == null || values.length == 0)
		{
			return 0L;
		}
		return redisTemplate.opsForList().rightPushAll(key, convertObjs(values));
	}

	@Override
	public <T> T lpop(String key, Class<T> clazz)
	{
		String value = redisTemplate.opsForList().leftPop(key);
		return convertObjs(value, clazz);
	}

	@Override
	public <T> T rpop(String key, Class<T> clazz)
	{
		String value = redisTemplate.opsForList().rightPop(key);
		return convertObjs(value, clazz);
	}

	@Override
	public <T> T blpop(String key, long timeout, Class<T> clazz)
	{
		String value = redisTemplate.opsForList().leftPop(key, timeout, TimeUnit.SECONDS);
		return convertObjs(value, clazz);
	}

	@Override
	public <T> T brpop(String key, long timeout, Class<T> clazz)
	{
		String value = redisTemplate.opsForList().rightPop(key, timeout, TimeUnit.SECONDS);
		return convertObjs(value, clazz);
	}

	@Override
	public boolean zadd(String key, double score, Object value)
	{
		return redisTemplate.opsForZSet().add(key, convertObjs(value), score);
	}
	
	@Override
	public List<String> zrange(String key, long start, long end)
	{
		return new ArrayList<String>(redisTemplate.opsForZSet().range(key, start, end));
	}

	@Override
	public <T> List<T> zrange(String key, long start, long end, Class<T> clazz)
	{
		List<T> ret = new ArrayList<T>();
		Set<String> values = redisTemplate.opsForZSet().range(key, start, end);
		if(values != null && values.size() > 0)
		{
			for(String value : values)
			{
				ret.add(convertObjs(value, clazz));
			}
		}
		return ret;
	}
	
	@Override
	public List<String> zrevrange(String key, long start, long end)
	{
		return new ArrayList<String>(redisTemplate.opsForZSet().reverseRange(key, start, end));
	}
	
	@Override
	public <T> List<T> zrevrange(String key, long start, long end, Class<T> clazz)
	{
		List<T> ret = new ArrayList<T>();
		Set<String> values = redisTemplate.opsForZSet().reverseRange(key, start, end);
		if(values != null && values.size() > 0)
		{
			for(String value : values)
			{
				ret.add(convertObjs(value, clazz));
			}
		}
		return ret;
	}

	@Override
	public long zrem(String key, Object... values)
	{
		return redisTemplate.opsForZSet().remove(key, values);
	}
	
	@Override
	public Set<String> zrangebyscore(String key, double min, double max)
	{
		return redisTemplate.opsForZSet().rangeByScore(key, min, max);
	}

	@Override
	public Set<String> zrangebyscore(String key, double min, double max, long offset, long count)
	{
		return redisTemplate.opsForZSet().rangeByScore(key, min, max, offset, count);
	}
	@Override
	public Set<?> zrangebyscoreWithScores(String key, double min, double max)
	{
		return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
	}
	@Override
	public Set<?> zrangebyscoreWithScores(String key, double min, double max, long offset, long count)
	{
		return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max, offset, count);
	}
	
	public void subscribe(String channel, MessageListener listener)
	{
		if(listener == null)
		{
			return;
		}
		if(StringUtils.isEmpty(channel))
		{
			return;
		}
		byte[] rawChannel = redisTemplate.getStringSerializer().serialize(channel);
		redisTemplate.getConnectionFactory().getConnection().subscribe(listener, rawChannel);
	}
	
	public void publish(String channel, Serializable message)
	{
		if(StringUtils.isEmpty(channel))
		{
			return;
		}
		if(message == null)
		{
			return;
		}
		redisTemplate.convertAndSend(channel, message);
	}

	private String convertObjs(Object value)
	{
		if (value == null)
		{
			return "";
		}
		if (value.getClass() == String.class 
		 || value.getClass() == Integer.class
		 || value.getClass() == int.class
		 || value.getClass() == Long.class
		 || value.getClass() == long.class
		 || value.getClass() == Double.class
		 || value.getClass() == double.class
		 || value.getClass() == Float.class
		 || value.getClass() == float.class
		 )
		{
			return value.toString();
		}
		else
		{
			return JSON.toJSONString(value);
		}
	}
	
	private String[] convertObjs(Object... values)
	{
		String[] strs = new String[values.length];
		for (int i = 0; i < values.length; i++)
		{
			Object value = values[i];
			if (value == null)
			{
				continue;
			}
			if (value.getClass() == String.class 
			 || value.getClass() == Integer.class
			 || value.getClass() == int.class
			 || value.getClass() == Long.class
			 || value.getClass() == long.class
			 || value.getClass() == Double.class
			 || value.getClass() == double.class
			 || value.getClass() == Float.class
			 || value.getClass() == float.class
			 )
			{
				strs[i] = value.toString();
			}
			else
			{
				strs[i] = JSON.toJSONString(values[i]);
			}
		}
		return strs;
	}
	
	@SuppressWarnings("unchecked")
	private <T> T convertObjs(String value, Class<T> clazz)
	{
		if(clazz == String.class)
		{
			return (T)value;
		}
		else if(clazz == Integer.class || clazz == int.class)
		{
			if(StringUtils.isEmpty(value))
			{
				return null;
			}
			return (T)new Integer(value);
		}
		else if(clazz == Long.class || clazz == long.class)
		{
			if(StringUtils.isEmpty(value))
			{
				return null;
			}
			return (T)new Long(value);
		}
		else if(clazz == Double.class || clazz == double.class)
		{
			if(StringUtils.isEmpty(value))
			{
				return null;
			}
			return (T)new Double(value);
		}
		else if(clazz == Float.class || clazz == float.class)
		{
			if(StringUtils.isEmpty(value))
			{
				return null;
			}
			return (T)new Float(value);
		}
		else
		{
			return JSON.parseObject(value, clazz);
		}
	}
	
}
