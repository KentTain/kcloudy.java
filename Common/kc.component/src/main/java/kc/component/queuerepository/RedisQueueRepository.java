package kc.component.queuerepository;

import kc.component.base.QueueActionType;
import kc.component.base.QueueEntity;
import kc.component.irepository.IQueueRepository;
import kc.component.util.IRedisHelper;
import kc.framework.enums.QueueType;
import kc.framework.tenant.Tenant;
import kc.framework.tenant.TenantConstant;
import kc.framework.util.SerializeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@lombok.extern.slf4j.Slf4j
@Component("RedisQueueRepository")
public class RedisQueueRepository<T extends QueueEntity> implements IQueueRepository<T> {
    private final String _serviceName = "KC.Component.QueueRepository.RedisRepository";
    @Autowired
    private IRedisHelper<T> redisHelper;
    private kc.framework.tenant.Tenant _tenant;
    public RedisQueueRepository() {
        _tenant = TenantConstant.DbaTenantApiAccessInfo;
    }
    public RedisQueueRepository(Tenant tenant) {
        _tenant = tenant != null ? tenant : TenantConstant.DbaTenantApiAccessInfo;
    }

    private String getQueueName(Class<? extends QueueEntity> tClazz){
        String tenantName = _tenant != null ? _tenant.getTenantName().toLowerCase() : TenantConstant.DbaTenantName.toLowerCase();
        return tenantName + "-" + tClazz.getTypeName().toLowerCase();
    }

    @Override
    public boolean ProcessQueueList(Class<? extends QueueEntity> tClazz, Function<T, QueueActionType> callback, Consumer<T> failCallback) {
        boolean success = true;
        String key = getQueueName(tClazz);
        List<T> queue = redisHelper.listFindAll(key);
        for (T message : queue) {
            if (callback == null) continue;

            if (message.getErrorCount() >= message.getMaxProcessErrorCount()) continue;

            success = isSuccess(key, callback, failCallback, success, message);
        }

        return success;
    }

    @Override
    public boolean ProcessQueue(Class<? extends QueueEntity> tClazz, Function<T, QueueActionType> callback, Consumer<T> failCallback) {
        boolean success = true;
        if (callback == null) return success;

        String key = getQueueName(tClazz);
        T message = redisHelper.listRPop(key);
        if (message == null) return success;

        if (message.getErrorCount() >= message.getMaxProcessErrorCount()) return success;

        success = isSuccess(key, callback, failCallback, success, message);

        return success;
    }

    private boolean isSuccess(String key, Function<T, QueueActionType> callback, Consumer<T> failCallback, boolean success, T message) {
        log.debug(_serviceName + " begin to process Queue....... " + key);
        QueueActionType queueActionType = callback.apply(message);
        log.debug(_serviceName + " end to process Queue....... " + key);

        //String jsonObject = SerializeHelper.ToJson(message);
        if (queueActionType == QueueActionType.DeleteAfterExecuteAction)
        {
            log.debug(String.format(".......%s begin to delete Queue[%s] when QueueActionType is %s ", _serviceName, key, QueueActionType.DeleteAfterExecuteAction));
            //redisHelper.listRPop(key);
            log.debug(String.format(".......%s end to delete Queue[%s] when QueueActionType is %s ", _serviceName, key, QueueActionType.DeleteAfterExecuteAction));
            if (message.isManuallyDelete())
            {
                message.setErrorCount(0);
                redisHelper.listLPush(key, message);
            }
        }
        else if (queueActionType == QueueActionType.FailedRepeatActon)
        {
            success = false;
            //redisHelper.listRPop(key);

            message.setErrorCount(message.getErrorCount() + 1);
            //最后一次执行Queue操作失败后，执行回调函数
            if (failCallback != null && message.getErrorCount() == message.getMaxProcessErrorCount())
            {
                failCallback.accept(message);
            }
            else
            {
                log.debug(String.format(".......%s begin to update Queue[%s]'s ErrorCount when QueueActionType is %s ", _serviceName, key, QueueActionType.FailedRepeatActon));
                redisHelper.listLPush(key, message);
                log.debug(String.format(".......%s end to update Queue[%s]'s ErrorCount when QueueActionType is %s ", _serviceName, key, QueueActionType.FailedRepeatActon));
            }
        }
        else if (queueActionType == QueueActionType.KeepQueueAction)
        {
            log.debug(String.format(".......%s begin to update Queue[%s] when QueueActionType is %s ", _serviceName, key, QueueActionType.KeepQueueAction));
            //redisHelper.listRPop(key);

            message.setErrorCount(0);
            redisHelper.listLPush(key, message);
            log.debug(String.format(".......%s end to update Queue[%s] when QueueActionType is %s ", _serviceName, key, QueueActionType.KeepQueueAction));
        }
        return success;
    }

    @Override
    public long GetMessageCount(Class<? extends QueueEntity> tClazz) {
        String key = getQueueName(tClazz);
        return redisHelper.listLength(key);
    }

    @Override
    public void AddMessage(T entity) {
        String key = getQueueName(entity.getClass());
        entity.setQueueName(key);
        entity.setQueueType(QueueType.Redis);
        redisHelper.listLPush(key, entity);
    }

    @Override
    public void ModifyMessage(T entity) {
        throw new UnsupportedOperationException("ModifyMessage");
    }

    @Override
    public void RemoveAllMessage(Class<? extends QueueEntity> tClazz) {
        String key = getQueueName(tClazz);
        redisHelper.del(key);
    }

    @Override
    public void RemoveTopMessage(Class<? extends QueueEntity> tClazz) {
        String key = getQueueName(tClazz);
        redisHelper.listRPop(key);
    }

    @Override
    public void RemoveMessage(T entity) {
        throw new UnsupportedOperationException("ModifyMessage");
    }

}
