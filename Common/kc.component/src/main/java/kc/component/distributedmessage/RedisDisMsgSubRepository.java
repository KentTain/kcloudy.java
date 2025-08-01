package kc.component.distributedmessage;

import kc.component.base.TopicEntity;
import kc.component.irepository.ISubscriptionRepository;
import kc.component.util.IRedisHelper;
import kc.framework.base.EntityBase;
import kc.framework.tenant.Tenant;
import kc.framework.tenant.TenantConstant;
import kc.framework.util.SerializeHelper;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Component("RedisDisMsgSubRepository")
@lombok.extern.slf4j.Slf4j
public class RedisDisMsgSubRepository<T extends EntityBase> implements MessageListener, ISubscriptionRepository<T> {
    private final String _serviceName = "kc.component.distributedmessage.RedisDisMsgSubRepository";

    private Tenant _tenant;
    public RedisDisMsgSubRepository() {
        _tenant = TenantConstant.DbaTenantApiAccessInfo;
    }
    public RedisDisMsgSubRepository(Tenant tenant) {
        _tenant = tenant != null ? tenant : TenantConstant.DbaTenantApiAccessInfo;
    }

    public String getTopicName(Class<? extends TopicEntity> tClazz){
        String tenantName = _tenant != null ? _tenant.getTenantName().toLowerCase() : TenantConstant.DbaTenantName.toLowerCase();
        return tenantName + "-" + tClazz.getTypeName().toLowerCase();
    }

    private String key;
    private Class<T> clazz;
    private List<String> subscriptions;
    private Function<T, Boolean> callback;
    private Consumer<String> failCallback;

    @Override
    public boolean ProcessTopic(Class<? extends TopicEntity> tClazz, List<String> subscriptions, Function<T, Boolean> callback, Consumer<String> failCallback) {
        this.clazz = (Class<T>)tClazz;
        this.key = getTopicName(tClazz);
        this.subscriptions = subscriptions;
        this.callback = callback;
        this.failCallback = failCallback;
        return false;
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        try {
            String body = new String(message.getBody());
            String channel = new String(message.getChannel());
            if (key.equalsIgnoreCase(channel)) {
                T result = SerializeHelper.FromJson(body, this.clazz);
                if (callback != null) {
                    callback.apply(result);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (failCallback != null) {
                failCallback.accept(ex.getMessage());
            }
        }
    }
}
