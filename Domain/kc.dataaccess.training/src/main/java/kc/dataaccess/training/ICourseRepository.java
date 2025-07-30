package kc.dataaccess.training;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kc.model.training.Course;

@Repository
public interface ICourseRepository extends JpaRepository<Course, Integer>{

	Course findByName(String name);
	
}
