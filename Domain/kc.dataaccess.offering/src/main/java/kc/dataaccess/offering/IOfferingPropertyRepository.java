package kc.dataaccess.offering;

import kc.model.offering.OfferingProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOfferingPropertyRepository extends JpaRepository<OfferingProperty, Integer>, JpaSpecificationExecutor<OfferingProperty> {

    List<OfferingProperty> findAllByOffering_OfferingId(int offeringId);

    @Modifying
    @Query("delete from OfferingProperty u where u.id in ?1")
    void deleteAllByIds(List<Integer> ids);

    void deleteByIdIn(List<Integer> ids);
}
