package kc.service.base;

import kc.framework.base.SeedEntity;
import kc.framework.tenant.Tenant;

public interface IServiceBase {
    Tenant getCurrentTenant();
    SeedEntity getRegularDateVal(String seqName, int step);
}
