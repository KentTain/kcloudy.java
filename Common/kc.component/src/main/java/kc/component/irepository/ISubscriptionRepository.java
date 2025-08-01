package kc.component.irepository;

import kc.component.base.TopicEntity;
import kc.framework.base.EntityBase;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public interface ISubscriptionRepository <V extends EntityBase> {
    boolean ProcessTopic(Class<? extends TopicEntity> tClazz, List<String> subscriptions, Function<V, Boolean> callback, Consumer<String> failCallback);
}

