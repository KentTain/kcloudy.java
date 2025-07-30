package kc.dataaccess.training;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import kc.model.training.Teacher;

@Repository
public interface ITeacherRepository extends JpaRepository<Teacher, Integer>, JpaSpecificationExecutor<Teacher> {
	@EntityGraph(value = "Graph.Teacher.Courses", type = EntityGraph.EntityGraphType.FETCH)
	Teacher findByTeacherId(int id);
	
	Teacher findByName(String name);
	
	@Override
	@EntityGraph(value = "Graph.Teacher.Courses", type = EntityGraph.EntityGraphType.FETCH)
	Page<Teacher> findAll(@Nullable Specification<Teacher> spec, Pageable pageable);
}
