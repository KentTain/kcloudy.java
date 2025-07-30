package kc.component.util;

import kc.component.base.QueueEntity;
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
public class RedisQueueTestObj extends QueueEntity implements Serializable {
    private String name;

    private String intro;

    private Date birthday;
}
