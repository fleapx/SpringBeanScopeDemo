package cn.xiaowenjie.springbeanscopedemo.beans;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
// FIXME 指定session作用域，如果不设置proxyMode会报错
// FIXME 设置为ScopedProxyMode.INTERFACES也会报错
@SessionScope
//@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionBean1 {

	@PostConstruct
	public void init() {
		System.out.println(" -----------SessionBean1.init()------------ ");
	}

	private int count;

	public int invokeCount() {
		return count++;
	}

}
