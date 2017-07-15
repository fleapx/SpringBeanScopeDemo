# SpringBeanScopeDemo

ѧϰspring session��ʹ�ú���spring boot�Զ���thread scope��

> * ԭ��
> * ʹ��session scope
> * �Զ���thread scope������copy�����磩
> * ע�� thread scope ʵ��
> * ʵ���Լ��� ThreadScoped ע��
> * TODO ���ڵķ������������
> * �ʼ�1����������ʱ���ɵ�class
> * �ʼ�2��control֧�ַ���json


# ԭ��
����ע���bean����һ�������࣬��controller��ע��ô����ࣨ��������ʹ�õ�ʱ���ڸ���scope��ʵ����ȥ��ȡ��ʵ��bean������ObjectFactory����

�磬�������Զ���thread scope������ʵ�ֵ�scope�ӿڵ�get������

```Java
@Override
public Object get(String name, ObjectFactory objectFactory) {
	Map scope = (Map) threadLocal.get();
	Object object = scope.get(name);
	System.out.println("ThreadScope.get(), name=" + name);

	if (object == null) {
		object = objectFactory.getObject();
		scope.put(name, object);
	}
	return object;
}
```

ʵ�������е�ʱ��ÿ�ζ������get�����������־���£������name�� `scopedTarget.threadBean1`

```
ThreadScope.get(), name=scopedTarget.threadBean1
```

# ʹ��session scope

2��
```Java
// FIXME ָ��session���������������proxyMode�ᱨ��
// FIXME ����ΪScopedProxyMode.INTERFACESҲ�ᱨ��
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
```

����
```Java
@SessionScope
```

ʹ�� `@SessionScope` ���ӷ��㣬����Ҫָ�� proxyMode������Ϊ��Ĭ��ֵ���� `ScopedProxyMode.TARGET_CLASS`��

# �Զ��� thread scope ʵ�֣�����copy�����磩

����� `cn.xiaowenjie.springbeanscopedemo.myscope.ThreadScope`��ʵ�� `org.springframework.beans.factory.config.Scope` �ӿڡ�

[����](https://github.com/xwjie/SpringBeanScopeDemo/blob/master/springbeanscopedemo/springbeanscopedemo/src/main/java/cn/xiaowenjie/springbeanscopedemo/myscope/ThreadScope.java)

# ע�� thread scope

spring bootע�����£�ע��һ�� `CustomScopeConfigurer` ���ɣ�

```
@Bean
public CustomScopeConfigurer customScope() {
	System.out.println("\n\n === register customScope()\n\n");
	CustomScopeConfigurer configurer = new CustomScopeConfigurer();

	Map<String, Object> threadScope = new HashMap<String, Object>();
	
	threadScope.put(ThreadScope.THREAD, new ThreadScope());
	configurer.setScopes(threadScope);

	return configurer;
}
```

# ʵ���Լ��� ThreadScoped ע��

��ͦ�򵥵ģ���Ҫ������ `@Scope(value = ThreadScope.THREAD, proxyMode = ScopedProxyMode.TARGET_CLASS)`��

```
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Qualifier
@Scope(value = ThreadScope.THREAD, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ThreadScoped {
	
}
```

���Բο� `@SessionScope` , �ٷ������������и�Ĭ��ֵ

```
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Scope(WebApplicationContext.SCOPE_SESSION)
public @interface SessionScope {

	/**
	 * Alias for {@link Scope#proxyMode}.
	 * <p>Defaults to {@link ScopedProxyMode#TARGET_CLASS}.
	 */
	@AliasFor(annotation = Scope.class)
	ScopedProxyMode proxyMode() default ScopedProxyMode.TARGET_CLASS;

}
```

����󣬾Ϳ���

```Java
@ThreadScoped
```

��������

```Java
//FIXME ���������������ã�����ָ������������Ч
@Scope(value = ThreadScope.THREAD, proxyMode = ScopedProxyMode.TARGET_CLASS)
```

> ����ָ������ʵ����CGLIB��������controllerע��ľ��ǵ����ˡ�

# TODO ���ڵķ������������

������ʹ���� `ThreadLocal` û��������tomcat�����߳����õ�ʱ���ʹ����ԭ����ʵ�����ҵ�sts��tomcatĬ��10���̣߳�ˢ11�ξͻᷢ��������⡣

��򵥿���ʹ�� `ServletRequestListener` ����request����

>������

# �ʼ�1����������ʱ���ɵ�class

ÿ�ζ��ǲ�ס���� `sun.misc.ProxyGenerator.saveGeneratedFiles`

```Java
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
	System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "c://class");// dirΪ�����ļ�·��
	props.put("net.sf.cglib.core.DebuggingClassWriter.traceEnabled", "true");
}
```

# �ʼ�2��control֧�ַ���json

```
// FIXME ���ӷ���map�ᱨ��
@Bean
public HttpMessageConverter<?> getHttpMessageConverter() {
	return new MappingJackson2HttpMessageConverter();
}
```

