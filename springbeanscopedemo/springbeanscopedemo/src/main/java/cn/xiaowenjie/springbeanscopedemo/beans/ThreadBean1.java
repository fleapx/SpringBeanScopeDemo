package cn.xiaowenjie.springbeanscopedemo.beans;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import cn.xiaowenjie.springbeanscopedemo.myscope.ThreadScoped;

@Component
@ThreadScoped
//FIXME 或者下面这样配置，必须指定代理，否则不生效
//@Scope(value = ThreadScope.THREAD, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ThreadBean1 {

	@PostConstruct
	public void init() {
		System.out.println(" -----------ThreadBean1.init()------------ ");
	}

	private int count;

	public int invokeCount() {
		System.out.println("thread id:" + Thread.currentThread().getId() + ", count:" + ++count);
		return count;
	}

}
