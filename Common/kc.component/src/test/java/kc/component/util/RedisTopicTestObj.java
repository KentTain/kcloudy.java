package kc.component.util;

import kc.component.base.QueueEntity;
import kc.component.base.TopicEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class RedisTopicTestObj extends TopicEntity<RedisQueueTestObj> implements Serializable {
    private String topicName;

    private String intro;

}
