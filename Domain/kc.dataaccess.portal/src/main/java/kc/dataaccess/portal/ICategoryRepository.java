package kc.dataaccess.portal;

import kc.database.repository.ITreeNodeRepository;
import kc.model.portal.OfferingCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends ITreeNodeRepository<OfferingCategory, Integer> {
    @Query("SELECT d FROM OfferingCategory d LEFT OUTER JOIN FETCH d.parentNode WHERE d.id = :id")
    OfferingCategory findWithParentById(int id);

    @Query("SELECT CASE WHEN COUNT(c.name) > 0 THEN true ELSE false END FROM OfferingCategory c WHERE c.name = :name")
    boolean existsByName(@Param("name") String name);

    @Query("SELECT CASE WHEN COUNT(c.name) > 0 THEN true ELSE false END FROM OfferingCategory c WHERE c.name = :name and c.id != :id")
    boolean existsByIdAndName(@Param("id") int id, @Param("name") String name);
}
