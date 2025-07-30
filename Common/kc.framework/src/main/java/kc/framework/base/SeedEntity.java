package kc.framework.base;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@javax.persistence.Entity
@Table(name = "cfg_SeedEntity")
@NamedStoredProcedureQueries({
//管理列表
@NamedStoredProcedureQuery(
        name = "GetRegularDateVal",
        procedureName = "Utility_GetRegularDateVal",
        resultClasses = {SeedEntity.class},
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "seqname", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "length", type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "currdate", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "step", type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "code", type = String.class)
        })
})
public class SeedEntity implements java.io.Serializable {

    private static final long serialVersionUID = 4764322586833840041L;

    @Id
    @Column(name = "SeedType")
    private String seedType;
    @Column(name = "SeedValue")
    private String seedValue;
    @Column(name = "SeedMin")
    private int seedMin;
    @Column(name = "SeedMax")
    private int seedMax;
}
