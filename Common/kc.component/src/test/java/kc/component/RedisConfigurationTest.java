package kc.component;

import kc.component.distributedmessage.RedisDisMsgSubRepository;
import kc.component.util.RedisConfigurationAbstract;
import kc.component.util.RedisTopicTestObj;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;

import java.util.HashMap;

@Disabled
@Configuration
@EnableCaching
public class RedisConfigurationTest extends RedisConfigurationAbstract {

    @Autowired
    private RedisDisMsgSubRepository<RedisTopicTestObj> testListener;

    @Override
    public HashMap<String, MessageListener> getMessageListeners() {
        HashMap<String, MessageListener> result = new HashMap<>();
        String key = testListener.getTopicName(RedisTopicTestObj.class);
        result.put(key, testListener);
        return result;
    }

}
