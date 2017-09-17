package cn.xiaowenjie.springbeanscopedemo.beans;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class BaseBean1 {

	@PostConstruct
	public void init() {
		System.out.println(" -----------BaseBean1.init()------------ ");
	}


}
