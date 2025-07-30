package kc.dataaccess.offering;

import kc.model.offering.CategoryManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryManagerRepository extends JpaRepository<CategoryManager, Integer>, JpaSpecificationExecutor<CategoryManager> {
    public List<CategoryManager> findAllByCategory_Id(int categoryId);
}
