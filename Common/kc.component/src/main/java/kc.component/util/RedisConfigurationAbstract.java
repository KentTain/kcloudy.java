package kc.component.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kc.framework.util.SerializeHelper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashMap;

@Configuration
@EnableCaching
public abstract class RedisConfigurationAbstract extends CachingConfigurerSupport {
    /**
     * 自定义缓存注解key的生成策略。默认的生成策略是看不懂的(乱码内容)
     * 通过Spring 的依赖注入特性进行自定义的配置注入并且此类是一个配置类可以更多程度的自定义配置
     * 这里是生成的key是：类全名.方法名 方法参数
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder prefix = new StringBuilder();
            prefix.append(target.getClass().getName().toLowerCase());
            prefix.append("-").append(method.getName().toLowerCase());
            StringBuilder sb = new StringBuilder();
            for (Object obj : params) {
                if (obj instanceof String
                        || obj instanceof Integer
                        || obj instanceof Float
                        || obj instanceof BigDecimal){
                    sb.append("-").append(obj.toString());
                } else {
                    String hashCode = String.valueOf(obj.hashCode());
                    sb.append("-").append(hashCode);
                }
            }
            return prefix.append(sb.toString());
        };
    }

    /**
     * 采用RedisCacheManager作为缓存管理器
     * @param factory
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        //以锁写入的方式创建RedisCacheWriter对象
        RedisCacheWriter writer = RedisCacheWriter.lockingRedisCacheWriter(factory);
        //设置缓存注解的缓存时间，缓存1小时
        Duration duration = Duration.ofSeconds(3600L);
        //创建一个RedisSerializationContext.SerializationPair给定的适配器pair
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = jackson2JsonRedisSerializer();
        RedisSerializationContext.SerializationPair pair = RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer);
        //创建CacheConfig
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair).entryTtl(duration);
        return new RedisCacheManager(writer, redisCacheConfiguration);
    }

    /**
     * 修改redisTemplate的序列化方式
     *
     * @param factory LettuceConnectionFactory
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        ////解决键、值序列化问题
        StringRedisTemplate template = new StringRedisTemplate(factory);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = jackson2JsonRedisSerializer();
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    private Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper mapper = SerializeHelper.getMapper();

        // 此项必须配置，Redis否则会报java.lang.ClassCastException: java.util.LinkedHashMap cannot be cast to XXX
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        jackson2JsonRedisSerializer.setObjectMapper(mapper);
        return jackson2JsonRedisSerializer;
    }

    /**
     * <h2>将消息监听器绑定到消息容器</h2>
     * */
    @Bean
    RedisMessageListenerContainer redisContainer(RedisConnectionFactory factory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        HashMap<String, MessageListener> listenerHashMap = getMessageListeners();
        listenerHashMap.forEach((key, value)-> {
            container.addMessageListener(value, new ChannelTopic(key));
        });
        return container;
    }

    public abstract HashMap<String, MessageListener> getMessageListeners();

//    @Bean(name = "redisTemplate")
//    public RedisTemplate<K, V> redisTemplate(LettuceConnectionFactory factory) {
//        factory.setShareNativeConnection(false);
//        //创建RedisTemplate对象
//        RedisTemplate<K, V> template = new RedisTemplate<K, V>();
//        template.setConnectionFactory(factory);
//        //设置key的序列化方式
//        template.setKeySerializer(keySerializer());
//        template.setHashKeySerializer(keySerializer());
//
//        //设置RedisTemplate的Value序列化方式Jackson2JsonRedisSerializer；默认是JdkSerializationRedisSerializer
//        template.setValueSerializer(valueSerializer());
//        template.setHashValueSerializer(valueSerializer());
//
//        template.afterPropertiesSet();
//        return template;
//    }

//    private RedisSerializer<String> keySerializer() {
//        return new StringRedisSerializer();
//    }
//
//    private RedisSerializer<Object> valueSerializer() {
//        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
//
//        ObjectMapper om = new ObjectMapper();
//        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        //解决时间序列化问题
//        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        om.registerModule(new JavaTimeModule());
//
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//        return jackson2JsonRedisSerializer;
//    }
}
