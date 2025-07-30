package kc.web.util;

import java.io.IOException;
import java.util.Map;


@lombok.extern.slf4j.Slf4j
public class TemplateUtil {
	/**
	 * thymeleaf渲染模板
	 * 
	 * @param template 模版名称
	 * @param params   参数
	 * @return
	 */
	public static String processTemplate(final String template, final Map<String, Object> params) {
		if (template == null || params == null)
			return null;

		java.io.StringWriter stringWriter = new java.io.StringWriter();
		try {
			// 构造模板引擎
			org.thymeleaf.templateresolver.ClassLoaderTemplateResolver resolver = new org.thymeleaf.templateresolver.ClassLoaderTemplateResolver();
			resolver.setPrefix("templates/temp/");// 模板所在目录，相对于当前classloader的classpath。
			resolver.setSuffix(".html");// 模板文件后缀
			resolver.setCharacterEncoding("UTF-8");

			org.thymeleaf.TemplateEngine engine = new org.thymeleaf.TemplateEngine();
			engine.setTemplateResolver(resolver);
			engine.isInitialized();

			// 构造上下文(Model)
			org.thymeleaf.context.Context context = new org.thymeleaf.context.Context();
			context.setVariables(params);

			// 渲染模板
			engine.process(template, context, stringWriter);
			stringWriter.flush();

			// 输出html
			return stringWriter.toString();
		} catch (Exception e) {
			log.error("------processTemplate " + template + ": ", e);
			return null;
		} finally {
			try {
				stringWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * thymeleaf渲染模板
	 * 
	 * @param templateString 模版字符串
	 * @param params         参数
	 * @return
	 */
	public static String processString(final String templateString, final Map<String, Object> params) {
		if (templateString == null || params == null)
			return null;

		java.io.StringWriter stringWriter = new java.io.StringWriter();
		try {
			// 构造模板引擎
			org.thymeleaf.templateresolver.StringTemplateResolver resolver = new org.thymeleaf.templateresolver.StringTemplateResolver();
			org.thymeleaf.TemplateEngine engine = new org.thymeleaf.TemplateEngine();
			engine.setTemplateResolver(resolver);
			engine.isInitialized();

			// 构造上下文(Model)
			org.thymeleaf.context.Context context = new org.thymeleaf.context.Context();
			context.setVariables(params);

			// 渲染模板
			engine.process(templateString, context, stringWriter);
			stringWriter.flush();

			// 输出html
			return stringWriter.toString();
		} catch (Exception e) {
			log.error("------processTemplate " + templateString + ": ", e);
			return null;
		} finally {
			try {
				stringWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
