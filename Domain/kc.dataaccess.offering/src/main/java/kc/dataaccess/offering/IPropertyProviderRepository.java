package kc.dataaccess.offering;

import kc.model.offering.PropertyProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPropertyProviderRepository extends JpaRepository<PropertyProvider, Integer>, JpaSpecificationExecutor<PropertyProvider> {
    public List<PropertyProvider> findAllByCategory_Id(int categoryId);
}
