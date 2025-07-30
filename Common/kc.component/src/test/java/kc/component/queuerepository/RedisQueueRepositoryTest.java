package kc.component.queuerepository;

import kc.component.base.QueueActionType;
import kc.component.irepository.IQueueRepository;
import kc.component.util.RedisQueueTestObj;
import kc.framework.GlobalConfig;
import kc.framework.TestBase;
import kc.framework.enums.SystemType;

import kc.framework.extension.StringExtensions;
import kc.framework.tenant.TenantConstant;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.Date;

@Disabled
@DisplayName("Redis队列单元测试")
@SpringBootTest
@ExtendWith(SpringExtension.class)
class RedisQueueRepositoryTest extends TestBase {
    private Logger logger = LoggerFactory.getLogger(RedisQueueRepositoryTest.class);
    @BeforeAll
    static void setUpBeforeClass() {
        intilize();
    }

    @AfterAll
    static void setDownAfterClass() {
        GlobalConfig.DatabaseConnectionString = "";
        GlobalConfig.MySqlConnectionString = "";

        tearDown();
    }

    @Autowired
    private IQueueRepository<RedisQueueTestObj> _redisQueueRepository;

    @Test
    void insertQueue_Test() throws Exception {
        RedisQueueTestObj user =  new RedisQueueTestObj();
        user.setName("1. Jerry");
        user.setIntro("InserQueue_Test!");
        user.setBirthday(new Date());
        _redisQueueRepository.AddMessage(user);
        Thread.sleep(500);

        user =  new RedisQueueTestObj();
        user.setName("2. Tom");
        user.setIntro("InserQueue_Test!");
        user.setBirthday(new Date());
        _redisQueueRepository.AddMessage(user);

        // assertEquals("aa", operations.get("com.neo.f").getUserName());
    }

    @Disabled
    @Execution(CONCURRENT)
    @RepeatedTest(3) //3为当前用例执行的次数
    @DisplayName("多线程处理队列")
    void processQueue_Test() {
        _redisQueueRepository.ProcessQueue(RedisQueueTestObj.class, callback -> {
            RedisQueueTestObj emailResult = callback;
            if (emailResult != null)
            {
                System.out.println(
                        String.format("---email object likes threadId send emailId (%s) to users: %s",
                                emailResult.getId(), emailResult.getName()));
            }
            return QueueActionType.DeleteAfterExecuteAction;
        }, failCallback ->{
            System.out.println("---get email is failed. " + failCallback.getErrorMessage());
        });
    }
}
