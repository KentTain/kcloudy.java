package kc.dataaccess.portal;

import kc.model.portal.Offering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOfferingRepository extends JpaRepository<Offering, Integer> , JpaSpecificationExecutor<Offering> {
    @Query("from Offering as o where o.offeringType = :type and bitand(offeringVersion,  1) > 0 or bitand(offeringVersion,  2) > 0")
    List<Offering> findAllFreeByType(@Param("type") int type);

    @Query("from Offering as o where o.offeringType = :type")
    List<Offering> findAllByTypeAndOfferingVersion(@Param("type") int type);

    @Query("from Offering as o where o.offeringType = :type")
    Page<Offering> findAllByType(Pageable pageable, @Param("type") int type);

    @Query("from Offering as o where o.offeringCode LIKE %:code%" +
            " and o.offeringName LIKE %:name%" +
            " and o.category.id = :categoryId" +
            " and o.status = :status ")
    Page<Offering> findAllByCodeAndNameAndStatusAndCategoryId(Pageable pageable, @Param("code") String code, @Param("name") String name, @Param("categoryId") Integer categoryId, @Param("status") Integer status);

    @Query("from Offering as o where o.offeringCode LIKE CONCAT('%',:code,'%')" +
            " and o.offeringName LIKE CONCAT('%',:name,'%')" +
            " and o.category.id = :categoryId")
    Page<Offering> findAllByCodeAndNameAndCategoryId(Pageable pageable, @Param("code") String code, @Param("name") String name, @Param("categoryId") Integer categoryId);

    @Query("from Offering as o where o.offeringCode LIKE CONCAT('%',:code,'%')" +
            " and o.offeringName LIKE CONCAT('%',:name,'%')" +
            " and o.status LIKE :status")
    Page<Offering> findAllByCodeAndNameAndStatus(Pageable pageable, @Param("code") String code, @Param("name") String name, @Param("status") Integer status);

    @Query("from Offering as o where o.offeringCode LIKE CONCAT('%',:code,'%')" +
            " and o.offeringName LIKE CONCAT('%',:name,'%')")
    Page<Offering> findAllByCodeAndName(Pageable pageable, @Param("code") String code, @Param("name") String name);

    @Query("SELECT CASE WHEN COUNT(c.offeringName) > 0 THEN true ELSE false END FROM Offering c WHERE c.offeringName = :name")
    boolean existsByName(@Param("name") String name);

    @Query("SELECT CASE WHEN COUNT(c.offeringName) > 0 THEN true ELSE false END FROM Offering c WHERE c.offeringName = :name and c.offeringId != :id")
    boolean existsByIdAndName(@Param("id") int id, @Param("name") String name);
}
