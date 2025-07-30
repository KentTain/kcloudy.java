package kc.service.base;

import kc.framework.base.SeedEntity;
import kc.framework.extension.DateExtensions;
import kc.framework.tenant.Tenant;
import kc.framework.tenant.TenantContext;
import kc.service.webapiservice.thridparty.IGlobalConfigApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

@Service
public abstract class ServiceBase implements IServiceBase {
    @PersistenceContext
    protected EntityManager entityManager;

    protected final IGlobalConfigApiService globalConfigApiService;

    @Autowired
    public ServiceBase(IGlobalConfigApiService globalConfigApiService) {
        this.globalConfigApiService = globalConfigApiService;
    }

    @Override
    public Tenant getCurrentTenant() {
        return TenantContext.getCurrentTenant();
    }
    protected String getCurrentTenantName() {
        return TenantContext.getCurrentTenant().getTenantName();
    }

    /**
     * 获取多个编号的实例：
     * int productCount = model.getProducts().size();
     * SeedEntity productEntity = getRegularDateVal("Product", productCount);
     * int min = productEntity.getSeedMin();
     * String seedValue = productEntity.getSeedValue();
     * String codePrefix = seedValue.substring(0, seedValue.length() - 5);
     * int i = 0;
     * for (Product product : model.getProducts()) {
     * String productCode = codePrefix + StringExtensions.padLeft(String.valueOf(min + i), 5, '0');
     * }
     *
     * @param seqName
     * @param step
     * @return
     */
    @Override
    public SeedEntity getRegularDateVal(String seqName, int step) {
        StoredProcedureQuery spQuery = entityManager.createNamedStoredProcedureQuery("GetRegularDateVal");

        String currDate = DateExtensions.getNowDateString("yyyy-MM-dd");
        StoredProcedureQuery storedProcedure = spQuery
                .setParameter("seqname", seqName)
                .setParameter("length", 5)
                .setParameter("currdate", currDate)
                .setParameter("step", step);

        return (SeedEntity) storedProcedure.getSingleResult();
    }
}
