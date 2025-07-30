package kc.web.util;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 获取spring容器，以访问容器中定义的其他bean <br/>
 * 类似Util类：Web容器--WebApplicationContextUtils <br/>
 * 使用范例: <br/>
 * public Application implements ApplicationListener<ContextRefreshedEvent> <br/>
 * 
 * public void onApplicationEvent(ContextRefreshedEvent event) {
 *		SpringContextUtil.setApplicationContext(event.getApplicationContext());
 * }
 * 
 */
public class SpringContextUtil {
	private static ApplicationContext applicationContext;

	/**
	 * 实现ApplicationContextAware接口的回调方法，设置上下文环境     
	 * 
	 * @param applicationContext     
	 */
	public static void setApplicationContext(ApplicationContext applicationContext) {
		if (null == SpringContextUtil.applicationContext)
			SpringContextUtil.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 主动向Spring容器中注册bean
	 *
	 * @param name  BeanName
	 * @param clazz 注册的bean的类性
	 * @param args  构造方法的必要参数，顺序和类型要求和clazz中定义的一致
	 * @param       <T>
	 * @return 返回注册到容器中的bean对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T setBean(String name, Class<T> clazz, Object... args) {
		ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) getApplicationContext();
		if (ctx.containsBean(name)) {
			Object bean = ctx.getBean(name);
			if (bean.getClass().isAssignableFrom(clazz)) {
				return (T) bean;
			} else {
				throw new RuntimeException("BeanName 重复 " + name);
			}
		}

		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
		for (Object arg : args) {
			beanDefinitionBuilder.addConstructorArgValue(arg);
		}
		BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();

		BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) ctx.getBeanFactory();
		beanFactory.registerBeanDefinition(name, beanDefinition);
		return ctx.getBean(name, clazz);
	}

	/**
	 *  通过name获取 Bean
	 * 
	 * @param name Bean的名称
	 * @return      
	 */
	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}

	/**
	 *   通过name获取 Bean
	 * 
	 * @param clazz Bean的类型
	 * @return      
	 */
	public static <T> T getBean(Class<T> clazz) {
		return getApplicationContext().getBean(clazz);
	}

	/**
	 * 通过name,以及Clazz返回指定的Bean
	 * 
	 * @param name  Bean的名称
	 * @param clazz Bean的类型
	 * @return      
	 */
	public static <T> T getBean(String name, Class<T> clazz) {
		return getApplicationContext().getBean(name, clazz);
	}

}