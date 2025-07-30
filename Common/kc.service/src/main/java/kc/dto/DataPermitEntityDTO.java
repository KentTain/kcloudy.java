package kc.dto;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public abstract class DataPermitEntityDTO extends EntityDTO implements Serializable {

    private String orgIds;

    private String roleIds;

    private String userIds;
}
