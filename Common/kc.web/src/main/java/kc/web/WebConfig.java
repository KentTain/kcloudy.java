package kc.web;

import kc.web.converter.StringToUUIDConverter;
import kc.web.converter.UniversalEnumConverterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

/**
 * Web配置
 * @author 田长军
 *
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        AntPathMatcher matcher = new AntPathMatcher();
        matcher.setCaseSensitive(false);
        configurer.setPathMatcher(matcher);
    }
	
	/**
	 * 添加类型转换器和格式化器
	 * 
	 * @param registry
	 */
	@Override
	public void addFormatters(FormatterRegistry registry) {
		// registry.addFormatterForFieldType(LocalDate.class, new
		// USLocalDateFormatter());
		registry.addConverter(new StringToUUIDConverter());
		registry.addConverterFactory(new UniversalEnumConverterFactory());
		//System.out.println("-----WebMvcConfig registry Converter-----");
	}

	/**
	 * 跨域支持
	 * 
	 * @param registry
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOriginPatterns("*")
				.allowCredentials(true)
				.allowedMethods("GET", "POST", "DELETE", "PUT")
				.maxAge(3600 * 24);

		//System.out.println("-----WebMvcConfig registry Cors-----");
	}
	

	/**
	 * 添加静态资源--过滤swagger-api (开源的在线API文档)
	 * 
	 * @param registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 过滤swagger
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

		registry.addResourceHandler("/swagger-resources/**")
				.addResourceLocations("classpath:/META-INF/resources/swagger-resources/");

		registry.addResourceHandler("/swagger/**").addResourceLocations("classpath:/META-INF/resources/swagger*");

		registry.addResourceHandler("/v2/api-docs/**")
				.addResourceLocations("classpath:/META-INF/resources/v2/api-docs/");

		//System.out.println("-----WebMvcConfig addResourceHandlers-----");
	}

	@Autowired
    private GlobalViewModelInterceptor interceptor;


	/**
	 * 实现自定义拦截器只需要3步: 1、创建我们自己的拦截器类并实现 HandlerInterceptor 接口。
	 * 2、创建一个Java类继承WebMvcConfigurer，并重写 addInterceptors 方法。
	 * 3、实例化我们自定义的拦截器，然后将对像手动添加到拦截器链中（在addInterceptors方法中添加）。
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		InterceptorRegistration registration = registry.addInterceptor(interceptor);
		// 拦截配置l
		registration.addPathPatterns("/**");
		// 排除配置
		//registration.excludePathPatterns("/css");

		//System.out.println("-----WebMvcConfig addInterceptors tenantInterceptor-----");
	}


	private ApplicationContext applicationContext;

	/**
	 * Thymeleaf模板资源解析器(自定义的需要做前缀绑定)
	 */
	@Bean
	@ConfigurationProperties(prefix = "spring.thymeleaf")
	public SpringResourceTemplateResolver templateResolver() {
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();

		templateResolver.setApplicationContext(this.applicationContext);
		templateResolver.setCharacterEncoding("UTF-8");
		return templateResolver;
	}

	/**
	 * Thymeleaf标准方言解释器
	 */
	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		//支持spring EL表达式
		templateEngine.setEnableSpringELCompiler(true);
		return templateEngine;
	}

	/**
	 * 视图解析器
	 */
	@Bean
	public ThymeleafViewResolver thymeleafViewResolver() {
		ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
		thymeleafViewResolver.setTemplateEngine(templateEngine());
		thymeleafViewResolver.setCharacterEncoding("UTF-8");
		//System.out.println("-----WebMvcConfig thymeleafViewResolver-----");
		return thymeleafViewResolver;
	}


}
