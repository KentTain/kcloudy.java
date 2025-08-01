package kc.component.irepository;

import kc.component.base.QueueActionType;
import kc.component.base.QueueEntity;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface IQueueRepository<T extends QueueEntity> {
    /**l
     * 处理所有消息队列列表
     * @param callback 处理每个消息队列的方法
     * @param failCallback 队列发生错误后的错误处理方法
     * @return
     */
    boolean ProcessQueueList(Class<? extends QueueEntity> tClazz, Function<T, QueueActionType> callback, Consumer<T> failCallback);
    /**
     * 处理单个消息队列
     * @param callback 处理每个消息队列的方法
     * @param failCallback 队列发生错误后的错误处理方法
     * @return
     */
    boolean ProcessQueue(Class<? extends QueueEntity> tClazz, Function<T, QueueActionType> callback, Consumer<T> failCallback);
    /**
     * 获取消息队列中消息的个数
     * @return
     */
    long GetMessageCount(Class<? extends QueueEntity> tClazz);

    /**
     * 添加条消息到队列中
     */
    void AddMessage(T entity);
    /**
     * 修改某条消息
     */
    void ModifyMessage(T entity);
    /**
     * 移除所有的消息
     */
    void RemoveAllMessage(Class<? extends QueueEntity> tClazz);
    /**
     * 移除最上方的消息
     */
    void RemoveTopMessage(Class<? extends QueueEntity> tClazz);
    /**
     * 移除消息
     */
    void RemoveMessage(T entity);
}
