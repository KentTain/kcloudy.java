package kc.component.util;

import kc.framework.util.SerializeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
@lombok.extern.slf4j.Slf4j
public class RedisHelper<T> implements IRedisHelper<T> {
    public String CustomKey = "kcloudy-";

    // 在构造器中获取redisTemplate实例, key(not hashKey) 默认使用String类型
    private RedisTemplate<String, T> redisTemplate;
    // 在构造器中通过redisTemplate的工厂方法实例化操作对象
    private ListOperations<String, T> listOperations;
    private ZSetOperations<String, T> zSetOperations;
    private SetOperations<String, T> setOperations;
    private ValueOperations<String, T> valueOperations;

    @Autowired
    public RedisHelper(RedisTemplate<String, T> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.listOperations = redisTemplate.opsForList();
        this.zSetOperations = redisTemplate.opsForZSet();
        this.setOperations = redisTemplate.opsForSet();
        this.valueOperations = redisTemplate.opsForValue();
    }

    @Override
    public Long listLPush(String key, T domain) {
        key = AddSysCustomKey(key);
        return listOperations.leftPush(key, domain);
    }
    @Override
    public T listRPop(String key) {
        key = AddSysCustomKey(key);
        return listOperations.rightPop(key);
    }
    @Override
    public T listRPop(String key, long validTime) {
        key = AddSysCustomKey(key);
        return listOperations.rightPop(key, validTime, TimeUnit.MILLISECONDS);
    }
    @Override
    public long listLength(String key) {
        key = AddSysCustomKey(key);
        return listOperations.size(key);
    }
    @Override
    public List<T> listFindAll(String key) {
        if (!redisTemplate.hasKey(key)) {
            return null;
        }
        key = AddSysCustomKey(key);
        return listOperations.range(key, 0, listOperations.size(key));
    }

    /**
     * Redis发布订阅  发布
     * @param pubChannel
     * @param value
     */
    public void publish(String pubChannel, String value) {
        //pubChannel = AddSysCustomKey(pubChannel);
        redisTemplate.convertAndSend(pubChannel, value);
    }
    /**
     * Redis发布订阅  发布
     * @param pubChannel
     * @param value
     */
    public void publish(String pubChannel, T value) {
        redisTemplate.convertAndSend(pubChannel, value);
    }

    /**
     * 设置值
     */
    @Override
    public boolean stringSet(String key, T value) {
        key = AddSysCustomKey(key);
        if (value instanceof String) {
            return stringSet(key, (String) value);
        }
        return stringSet(key, SerializeHelper.ToJson(value));
    }
    /**
     * 设置值
     */
    @Override
    public boolean stringSet(String key, T value, long validTime) {
        key = AddSysCustomKey(key);
        if (value instanceof String) {
            return stringSet(key, (String) value, validTime);
        }
        return stringSet(key, SerializeHelper.ToJson(value), validTime);
    }
    /**
     * 设置值
     */
    private boolean stringSet(String key, String value) {
        Boolean res = this.redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            RedisSerializer<String> serializer = this.redisTemplate.getStringSerializer();
            connection.set(Objects.requireNonNull(serializer.serialize(key)), Objects.requireNonNull(serializer.serialize(value)));
            return true;
        });
        return res != null && res;
    }
    /**
     * 设置值
     */
    private boolean stringSet(String key, String value, long validTime) {
        Boolean res = this.redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            RedisSerializer<String> serializer = this.redisTemplate.getStringSerializer();
            byte[] keyByte = Objects.requireNonNull(serializer.serialize(key));
            byte[] valueByte = Objects.requireNonNull(serializer.serialize(value));
            connection.set(keyByte, valueByte);
            connection.expire(keyByte, validTime);
            return true;
        });
        return res != null && res;
    }

    /**
     * 获取值
     */
    @Override
    public T stringGet(String key, Class<T> clazz) {
        key = AddSysCustomKey(key);
        return SerializeHelper.FromJson(stringGetValue(key), clazz);
    }
    /**
     * 获取值
     */
    @Override
    public String stringGet(String key) {
        key = AddSysCustomKey(key);
        return stringGetValue(key);
    }
    /**
     * 获取值
     */
    private String stringGetValue(String key) {
        return this.redisTemplate.execute((RedisCallback<String>) connection -> {
            RedisSerializer<String> serializer = this.redisTemplate.getStringSerializer();
            byte[] value = connection.get(Objects.requireNonNull(serializer.serialize(key)));
            return serializer.deserialize(value);
        });
    }

    /**
     * 删除值
     */
    @Override
    public void del(String key) {
        key = AddSysCustomKey(key);
        this.redisTemplate.delete(key);
    }
    /**
     * 批量删除相同前缀的key
     */
    @Override
    public void batchDel(String prefix) {
        Set<String> keys = keys(prefix);
        if (null != keys && !keys.isEmpty()) {
            this.redisTemplate.delete(keys);
        }
    }
    /**
     * 批量删除
     */
    @Override
    public void batchDel(Collection<String> keys) {
        this.redisTemplate.delete(keys);
    }

    /**
     * 判断值缓存key是否存在
     */
    @Override
    public boolean exist(String key) {
        key = AddSysCustomKey(key);
        Boolean res = this.redisTemplate.hasKey(key);
        return res != null && res;
    }

    /**
     * 获取相同前缀的key
     */
    @Override
    public Set<String> keys(String prefix) {
        return this.redisTemplate.keys(prefix + "*");
    }

    /**
     * 设置某个值的缓存时间
     */
    @Override
    public boolean setExpire(String key, long validTime) {
        Boolean res = this.redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            RedisSerializer<String> serializer = this.redisTemplate.getStringSerializer();
            byte[] keyByte = Objects.requireNonNull(serializer.serialize(AddSysCustomKey(key)));
            connection.expire(keyByte, validTime);
            return true;
        });
        return res != null && res;
    }

    /**
     * 如果key不存在则设置，此方法使用了redis的原子性
     */
    @Override
    public boolean setNx(String key, T value, long validTime) {
        return setNx(key, value, validTime, TimeUnit.SECONDS);
    }

    /**
     * 如果key不存在则设置，此方法使用了redis的原子性
     */
    @Override
    public boolean setNx(String key, T value, long validTime, TimeUnit timeUnit) {
        try {
            key = AddSysCustomKey(key);
            Boolean lock = valueOperations.setIfAbsent(key, value, validTime, timeUnit);
            return lock != null && lock;
        } catch (Exception e) {
            this.del(key);
            e.printStackTrace();
        }
        return false;
    }

    private String AddSysCustomKey(String oldKey)
    {
        return CustomKey + oldKey;
    }
}
