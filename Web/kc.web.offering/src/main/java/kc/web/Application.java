package kc.web;

import kc.web.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import kc.database.repository.TreeNodeRepositoryFactoryBean;

@SpringBootApplication
@ServletComponentScan
@EntityScan({"kc.framework.base", "kc.model.offering"})
@ComponentScan({"kc.mapping.*", "kc.service.*", "kc.service.webapiservice", "kc.web.*", "kc.web"})
@EnableJpaRepositories(basePackages = {"kc.dataaccess.*", "kc.database"}, 
	repositoryFactoryBeanClass = TreeNodeRepositoryFactoryBean.class)
public class Application extends kc.web.WebSecurityConfig implements  EnvironmentAware {
	@Autowired
	public static Environment Env;
	@Override
	public void setEnvironment(Environment environment) {
		Application.Env = environment;
	}
	
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		app.addListeners(new ApplicationPidFileWriter());
		//动态注入Bean的工具类的使用：https://www.cnblogs.com/Chary/p/14361830.html
		ApplicationContext appContext =  app.run(args);
		SpringContextUtil.setApplicationContext(appContext);

        GlobalConfigInitializer.initWebAfterStarted("kc.web.controller", Env);
	}

}
