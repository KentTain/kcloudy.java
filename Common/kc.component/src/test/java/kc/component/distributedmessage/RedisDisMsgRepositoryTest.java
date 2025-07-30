package kc.component.distributedmessage;


import kc.component.irepository.ISubscriptionRepository;
import kc.component.irepository.ITopicRepository;
import kc.component.util.IRedisHelper;
import kc.component.util.RedisQueueTestObj;
import kc.component.util.RedisTopicTestObj;
import kc.framework.GlobalConfig;
import kc.framework.TestBase;
import kc.framework.enums.SystemType;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Disabled
@DisplayName("Redis分布式消息单元测试")
@SpringBootTest
@ExtendWith(SpringExtension.class)
class RedisDisMsgRepositoryTest extends TestBase {
    private Logger logger = LoggerFactory.getLogger(RedisDisMsgRepositoryTest.class);
    @BeforeAll
    static void setUpBeforeClass() {
        intilize();
    }

    @AfterAll
    static void setDownAfterClass() {
        tearDown();
    }

    @Autowired
    private ITopicRepository<RedisTopicTestObj> _redisPubRepository;
    @Autowired
    private ISubscriptionRepository<RedisTopicTestObj> _redisSubRepository;

    @Test
    void pubTopic_Test() throws Exception {
        List<String> channels = Collections.singletonList("RedisTopicTestObj");
        _redisSubRepository.ProcessTopic(
                RedisTopicTestObj.class,
                channels,
                callback -> {
                    RedisTopicTestObj emailResult = callback;
                    if (emailResult != null)
                    {
                        System.out.println(
                                String.format("---email object likes threadId send emailId (%s) to users: %s",
                                        emailResult.getId(), emailResult.getTopicName()));
                    }
                    return true;
                },
                failCallback ->{
                    System.out.println("---get email is failed. " + failCallback);
                });

        RedisQueueTestObj user1 =  new RedisQueueTestObj();
        user1.setName("1. Jerry");
        user1.setIntro("InsertQueue_Test!");
        user1.setBirthday(new Date());
        RedisTopicTestObj topic1 =  new RedisTopicTestObj();
        topic1.setTopicName("1. Topic");
        topic1.setIntro("pubTopic_Test!");
        topic1.setTopicContext(user1);
        _redisPubRepository.CreateTopic(topic1);
        Thread.sleep(500);

        RedisQueueTestObj user2 =  new RedisQueueTestObj();
        user2.setName("2. Tom");
        user2.setIntro("InsertQueue_Test!");
        user2.setBirthday(new Date());
        RedisTopicTestObj topic2 =  new RedisTopicTestObj();
        topic2.setTopicName("2. Topic");
        topic2.setIntro("pubTopic_Test!");
        topic2.setTopicContext(user2);
        _redisPubRepository.CreateTopic(topic2);

        // Assert.assertEquals("aa", operations.get("com.neo.f").getUserName());
    }
}
