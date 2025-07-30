package kc.component.distributedmessage;

import kc.component.base.QueueEntity;
import kc.component.base.TopicEntity;
import kc.component.irepository.ISubscriptionRepository;
import kc.component.irepository.ITopicRepository;
import kc.component.util.IRedisHelper;
import kc.framework.base.EntityBase;
import kc.framework.tenant.Tenant;
import kc.framework.tenant.TenantConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("RedisDisMsgPubRepository")
@lombok.extern.slf4j.Slf4j
public class RedisDisMsgPubRepository<T extends TopicEntity> implements ITopicRepository<T> {
    private final String _serviceName = "kc.component.distributedmessage.RedisDisMsgPubRepository";

    @Autowired
    private IRedisHelper<T> redisHelper;
    private kc.framework.tenant.Tenant _tenant;
    public RedisDisMsgPubRepository() {
        _tenant = TenantConstant.DbaTenantApiAccessInfo;
    }
    public RedisDisMsgPubRepository(Tenant tenant) {
        _tenant = tenant != null ? tenant : TenantConstant.DbaTenantApiAccessInfo;
    }

    private String getTopicName(Class<? extends TopicEntity> tClazz){
        String tenantName = _tenant != null ? _tenant.getTenantName().toLowerCase() : TenantConstant.DbaTenantName.toLowerCase();
        return tenantName + "-" + tClazz.getTypeName().toLowerCase();
    }
    @Override
    public void CreateTopic(T entity) {
        String channelName = getTopicName(entity.getClass());
        redisHelper.publish(channelName, entity);
        System.out.println("RedisDisMsgPubRepository: " + channelName);
    }


}
