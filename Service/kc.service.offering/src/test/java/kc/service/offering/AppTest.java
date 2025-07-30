package kc.service.offering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import kc.database.repository.TreeNodeRepositoryFactoryBean;

@SpringBootApplication()
@ComponentScan({"kc.service.*", "kc.mapping.*"})
@EntityScan({"kc.framework.base", "kc.model.*"})
@EnableJpaRepositories(basePackages = {"kc.dataaccess.*", "kc.database"}, repositoryFactoryBeanClass = TreeNodeRepositoryFactoryBean.class)
public class AppTest {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(AppTest.class, args);

	}
}
