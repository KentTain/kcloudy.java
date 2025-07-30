package kc.dataaccess.dict;

import kc.framework.base.ConfigAttribute;
import kc.model.dict.City;
import kc.model.dict.Province;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICityRepository extends JpaRepository<City, Integer>{

	City findDetailByName(String name);

	City findDetailById(int it);

	@Query("FROM City c where c.province.provinceId = :provinceId")
	List<City> findByProvinceId(@Param("provinceId") int provinceId);
}
