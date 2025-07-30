package kc.service.codegenerate;

import kc.framework.GlobalConfig;
import kc.framework.GlobalConfigData;
import kc.service.webapiservice.thridparty.impl.GlobalConfigApiService;
import kc.web.GlobalConfigInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import kc.database.repository.TreeNodeRepositoryFactoryBean;

@SpringBootApplication()
@ComponentScan({"kc.service.*", "kc.mapping.*"})
@EntityScan({"kc.framework.base", "kc.model.*"})
@EnableJpaRepositories(basePackages = {"kc.dataaccess.*", "kc.database"}, repositoryFactoryBeanClass = TreeNodeRepositoryFactoryBean.class)
public class AppTest implements EnvironmentAware {
    @Autowired
    public static Environment Env;

    @Override
    public void setEnvironment(Environment environment) {
        AppTest.Env = environment;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=====Start to test the spring boot=====");
        SpringApplication.run(AppTest.class, args);

        System.out.println("=====Init the global config=====");
        GlobalConfig.InitGlobalConfig(Env);
        GlobalConfigData configData = new GlobalConfigApiService().GetGlobalConfigData();
        GlobalConfig.InitGlobalConfigWithApiData(Env, configData);

        System.out.println("=====End to test the spring boot=====");
    }
}
