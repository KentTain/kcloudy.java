package kc.dataaccess.config;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication()

@EntityScan({"kc.framework.base", "kc.model.config"})
//@EnableJpaRepositories(repositoryFactoryBeanClass = TreeNodeRepositoryFactoryBean.class)
public class AppTest {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(AppTest.class, args);

	}
}
