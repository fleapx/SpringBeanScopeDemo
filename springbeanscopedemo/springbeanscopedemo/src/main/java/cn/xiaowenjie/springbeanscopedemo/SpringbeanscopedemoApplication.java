package cn.xiaowenjie.springbeanscopedemo;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import cn.xiaowenjie.springbeanscopedemo.myscope.ThreadScope;

@AutoConfigureWebMvc
@SpringBootApplication
public class SpringbeanscopedemoApplication {

	public static void main(String[] args) throws Exception {
		saveGeneratedJdkProxyFiles();
		saveGeneratedCGlibProxyFiles();

		SpringApplication.run(SpringbeanscopedemoApplication.class, args);
	}

	@Bean
	public CustomScopeConfigurer customScope() {
		System.out.println("\n\n === register customScope()\n\n");
		CustomScopeConfigurer configurer = new CustomScopeConfigurer();

		Map<String, Object> threadScope = new HashMap<String, Object>();
		
		threadScope.put(ThreadScope.THREAD, new ThreadScope());
		configurer.setScopes(threadScope);

		return configurer;
	}

	// FIXME 不加返回map会报错
	@Bean
	public HttpMessageConverter<?> getHttpMessageConverter() {
		return new MappingJackson2HttpMessageConverter();
	}

	public static void saveGeneratedJdkProxyFiles() throws Exception {
		Field field = System.class.getDeclaredField("props");
		field.setAccessible(true);
		Properties props = (Properties) field.get(null);
		props.put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
	}

	public static void saveGeneratedCGlibProxyFiles() throws Exception {
		Field field = System.class.getDeclaredField("props");
		field.setAccessible(true);
		Properties props = (Properties) field.get(null);
		System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "c://class");// dir为保存文件路径
		props.put("net.sf.cglib.core.DebuggingClassWriter.traceEnabled", "true");
	}
}
