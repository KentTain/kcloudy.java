package kc.dataaccess.app;

import kc.model.app.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IApplicationRepository extends JpaRepository<Application, String>, JpaSpecificationExecutor<Application> {

	Application getApplicationByApplicationNameContains(String name);

	Page<Application> findAllByApplicationNameContains(Pageable pageable, String name);
}
