package kc.component.util;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

@Disabled
@SpringBootTest
@ExtendWith(SpringExtension.class)
class RedisHelperTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void redisTemplateTest() throws Exception {
        RedisQueueTestObj user = new RedisQueueTestObj();
        user.setName("Jerry");
        user.setIntro("redisTemplateTest!");
        user.setBirthday(new Date());
        ValueOperations<String, RedisQueueTestObj> operations = redisTemplate.opsForValue();
        operations.set("redisTemplateTest", user);
        Thread.sleep(500);
        boolean exists = redisTemplate.hasKey("redisTemplateTest");
        if (exists) {
            System.out.println(redisTemplate.opsForValue().get("redisTemplateTest"));
        } else {
            System.out.println("exists is false");
        }
        // Assert.assertEquals("aa", operations.get("com.neo.f").getUserName());
    }

    @Autowired
    private IRedisHelper redisHelper;

    @Test
    void redisHelperTest() throws Exception {
        RedisQueueTestObj user1 = new RedisQueueTestObj();
        user1.setName("Alex");
        user1.setIntro("redisHelperTest!");
        user1.setBirthday(new Date());
        redisHelper.stringSet("redisHelperTest", user1);
        System.out.println(redisHelper.stringGet("user1"));

        RedisQueueTestObj user2 = new RedisQueueTestObj();
        user2.setName("Bob");
        user2.setIntro("redisHelperTest!");
        user2.setBirthday(new Date());
        redisHelper.stringSet("redisHelperTest", user2);
        System.out.println(redisHelper.stringGet("user2"));
    }


}


