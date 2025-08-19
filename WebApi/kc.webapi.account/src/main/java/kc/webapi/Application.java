package kc.webapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import kc.database.repository.TreeNodeRepositoryFactoryBean;

@SpringBootApplication
@ServletComponentScan
@EntityScan({ "kc.framework.base", "kc.model.account" })
@ComponentScan({ "kc.mapping.*", "kc.service.*", "kc.service.webapiservice", "kc.web.*", "kc.web", "kc.webapi.*" })
@EnableJpaRepositories(basePackages = { "kc.dataaccess.*",
		"kc.database" }, repositoryFactoryBeanClass = TreeNodeRepositoryFactoryBean.class)
public class Application extends kc.web.WebApiSecurityConfig {
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		app.addListeners(new ApplicationPidFileWriter());
		app.run(args);
	}
}