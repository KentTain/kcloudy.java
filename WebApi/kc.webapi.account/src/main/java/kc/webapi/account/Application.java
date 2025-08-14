package kc.webapi.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import kc.database.repository.TreeNodeRepositoryFactoryBean;
import kc.web.GlobalConfigInitializer;

@SpringBootApplication
@ServletComponentScan

@EntityScan({ "kc.framework.base", "kc.model" })
@ComponentScan({ "kc.mapping.*", "kc.service.*", "kc.service.webapiservice", "kc.web.*", "kc.web.multitenancy",
		"kc.webapi.*" })
@EnableJpaRepositories(basePackages = { "kc.dataaccess.*",
		"kc.database" }, repositoryFactoryBeanClass = TreeNodeRepositoryFactoryBean.class)
public class Application extends kc.web.WebApiSecurityConfig implements EnvironmentAware {
	@Autowired
	public static Environment Env;

	@Override
	public void setEnvironment(Environment environment) {
		Application.Env = environment;
	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		app.addListeners(new ApplicationPidFileWriter());
		app.run(args);

		GlobalConfigInitializer.initWebAfterStarted("kc.webapi.controller", Env);
	}

}
