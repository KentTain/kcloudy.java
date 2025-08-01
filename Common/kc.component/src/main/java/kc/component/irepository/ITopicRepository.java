package kc.component.irepository;

import kc.component.base.TopicEntity;
import kc.framework.base.EntityBase;

public interface ITopicRepository<T extends TopicEntity>  {
    void CreateTopic(T entity);
}
