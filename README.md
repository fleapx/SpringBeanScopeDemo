# SpringBeanScopeDemo

学习spring session的使用和在spring boot自定义thread scope。

> * 原理
> * 使用session scope
> * 自定义thread scope（代码copy自网络）
> * 注册 thread scope 实现
> * 实现自己的 ThreadScoped 注解
> * TODO 现在的范例代码的问题
> * 笔记1：保存运行时生成的class
> * 笔记2：control支持返回json


# 原理
根据注入的bean生成一个代理类，在controller中注入该代理类（单例），使用的时候在根据scope的实现类去获取真实的bean（调用ObjectFactory）。

如，下面是自定义thread scope代码中实现的scope接口的get方法。

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

实际上运行的时候，每次都会调用get方法，输出日志如下，传入的name是 `scopedTarget.threadBean1`

```
ThreadScope.get(), name=scopedTarget.threadBean1
```

# 使用session scope

2种
```Java
// FIXME 指定session作用域，如果不设置proxyMode会报错
// FIXME 设置为ScopedProxyMode.INTERFACES也会报错
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
```

或者
```Java
@SessionScope
```

使用 `@SessionScope` 更加方便，不需要指定 proxyMode，是因为他默认值就是 `ScopedProxyMode.TARGET_CLASS`。

# 自定义 thread scope 实现（代码copy自网络）

代码见 `cn.xiaowenjie.springbeanscopedemo.myscope.ThreadScope`。实现 `org.springframework.beans.factory.config.Scope` 接口。

[代码](https://github.com/xwjie/SpringBeanScopeDemo/blob/master/springbeanscopedemo/springbeanscopedemo/src/main/java/cn/xiaowenjie/springbeanscopedemo/myscope/ThreadScope.java)

# 注册 thread scope

spring boot注册如下，注册一个 `CustomScopeConfigurer` 即可：

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

# 实现自己的 ThreadScoped 注解

还挺简单的，主要是增加 `@Scope(value = ThreadScope.THREAD, proxyMode = ScopedProxyMode.TARGET_CLASS)`。

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

可以参考 `@SessionScope` , 官方的是在里面有个默认值

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

定义后，就可以

```Java
@ThreadScoped
```

或者这样

```Java
//FIXME 或者下面这样配置，必须指定代理，否则不生效
@Scope(value = ThreadScope.THREAD, proxyMode = ScopedProxyMode.TARGET_CLASS)
```

> 必须指定代理实例（CGLIB），否则controller注入的就是单例了。

# TODO 现在的范例代码的问题

问题是使用了 `ThreadLocal` 没有清理，在tomcat容器线程重用的时候会使用了原来的实例，我的sts上tomcat默认10个线程，刷11次就会发现这个问题。

最简单可以使用 `ServletRequestListener` 监听request清理。

>待讨论

# 笔记1：保存运行时生成的class

每次都记不住名字 `sun.misc.ProxyGenerator.saveGeneratedFiles`

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
	System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "c://class");// dir为保存文件路径
	props.put("net.sf.cglib.core.DebuggingClassWriter.traceEnabled", "true");
}
```

# 笔记2：control支持返回json

```
// FIXME 不加返回map会报错
@Bean
public HttpMessageConverter<?> getHttpMessageConverter() {
	return new MappingJackson2HttpMessageConverter();
}
```

