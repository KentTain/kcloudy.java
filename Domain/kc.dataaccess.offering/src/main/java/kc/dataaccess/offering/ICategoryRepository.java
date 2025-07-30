package kc.dataaccess.offering;

import kc.database.repository.ITreeNodeRepository;
import kc.model.offering.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends ITreeNodeRepository<Category, Integer> {
    @Query("SELECT d FROM Category d LEFT OUTER JOIN FETCH d.parentNode WHERE d.id = :id")
    Category findWithParentById(int id);

    @Query("from Category as o where o.offeringPriceType = :priceType and bitand(offeringPropertyType,  :propertyType) = :propertyType")
    List<Category> findAllByPriceTypeAndPropertyTypes(@Param("priceType") int priceType, @Param("propertyType") Integer propertyType);

    @Query("from Category as o where o.name LIKE CONCAT('%',:name,'%')" +
            " and bitand(o.offeringPropertyType,  :propertyType) > 0")
    Page<Category> findAllByFilter(Pageable pageable, @Param("name") String name, @Param("propertyType") Integer propertyType);


    @Query("SELECT CASE WHEN COUNT(c.name) > 0 THEN true ELSE false END FROM Category c WHERE c.name = :name")
    boolean existsByName(@Param("name") String name);

    @Query("SELECT CASE WHEN COUNT(c.name) > 0 THEN true ELSE false END FROM Category c WHERE c.name = :name and c.id != :id")
    boolean existsByIdAndName(@Param("id") int id, @Param("name") String name);
}
