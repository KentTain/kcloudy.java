package kc.dataaccess.offering;

import kc.model.offering.Product;
import kc.model.offering.ProductProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductPropertyRepository extends JpaRepository<ProductProperty, Integer>, JpaSpecificationExecutor<ProductProperty> {

    List<ProductProperty> findAllByProduct_ProductId(int productId);

    @Modifying
    @Query("delete from ProductProperty u where u.id in ?1")
    void deleteAllByIds(List<Integer> ids);

    void deleteByIdIn(List<Integer> ids);
}
