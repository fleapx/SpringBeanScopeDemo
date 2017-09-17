package cn.xiaowenjie.springbeanscopedemo;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.xiaowenjie.springbeanscopedemo.beans.BaseBean1;

@Service
// @Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MainServices {

	@Autowired
	BaseBean1 bean1;

	@PostConstruct
	public void init() {
		System.out.println(" ===-------MainServices.init()------------ ");
	}

	private int count;

	public int invokeCount() {
		System.out.println("bean1=" + bean1);
		return count++;
	}

	public int invokeCount2() {
		return count++;
	}
}
